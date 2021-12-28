package newfsmengine

import (
	"mytest.com/src/common/constant"
	"mytest.com/src/common/errors"
	_ "mytest.com/src/common/log"
	"mytest.com/src/common/utils"
	_ "mytest.com/src/common/utils"
	pb "mytest.com/src/proto"
	"git.code.oa.com/trpc-go/trpc-go/errs"
	"github.com/spf13/cast"
	"strings"
	"time"
)

//func (s *releaseEngineServiceImpl) execUserEvent(syncMemTask *syncMemTask) bool {
//	//暂停与回滚不一样 暂停 只需要打标记,回滚除了打标记还要做操作
//	userEventKey := fmt.Sprintf("%v_%v", syncMemTask.data.GetTaskId(), syncMemTask.data.GetSubTaskId())
//	if userEventChan, ok := s.userEventsMap.Load(userEventKey); ok {
//		// 设定指定批次 fsm中去对批次判断
//		batchNumAction := strings.Split(userEvent.(string), "_")
//		if len(batchNumAction[0]) == 0 { //所有批次
//			//s.dealTask(-1, task, taskNewStatus)
//			unmarshalTaskPlanInfo()
//			syncMemTask.data
//			return
//		} else {
//			batchNums := strings.Split(batchNumAction[0], ",")
//			actionCode, err1 := strconv.Atoi(batchNumAction[1])
//			for bn := range batchNums {
//				syncMemTask.userSetBatchNum = append(syncMemTask.userSetBatchNum, cast.ToInt32(bn))
//			}
//
//			//如果第一次启动需要手动触发trigger,后期接受事件只能是在执行中的流程中打断暂停
//			if syncMemTask.data.GetStatus() == constant.UnDeploy {
//				err1 = s.fsm.Trigger(syncMemTask.data.GetStatus(), transWebEvent(cast.ToInt32(actionCode)), syncMemTask)
//			} else {
//
//			}
//
//			s.userEventsMap.Delete(userEventKey)
//
//		}
//		return true
//	}
//	return false
//}

func (s *releaseEngineServiceImpl) queryUserOperations() ([]*pb.Operation, error) {
	var err error
	// 批量更新已死亡的调度者的状态
	tx := s.Store.BeginTx()
	defer s.Store.EndTx(tx, &err)
	// 获取当前为存活状态的调度者
	userEvents, err := s.Store.Operation(tx).SchedulerQueryUserOperations(s.schedulerName, s.paramVersion)
	return userEvents, err
}

//TODO 完善用户事件回写完成逻辑
func (s *releaseEngineServiceImpl) updateUserOperationDone(operationId int32) error {
	var err error
	tx := s.Store.BeginTx()
	defer s.Store.EndTx(tx, &err)
	err = s.Store.Operation(tx).UpdateUserOperationDone(operationId)
	return nil
}


//scheduleLoadingUserEvents 调度核心逻辑 拉取未完成的task获取对应的批次执行发布任务
func (s *releaseEngineServiceImpl) scheduleLoadingUserEvents() error {
	//ticker := time.NewTicker(constant.SchedulerLoadingUserEventsPeriod)
	ticker := time.NewTicker(constant.SchedulerLoadingUserEventsPeriod * 5)
	go func() {
		for range ticker.C {
			s.log("scheduleLoadingUserEvents", "loading begin", " time:%v", utils.CurrentTimestampToInt())
			userEvents, err := s.queryUserOperations()
			if err != nil {
				errs := errs.New(errors.SchedulerQueryTaskBatchError, err.Error())
				log.Error("scheduler query task error: ", errs.Error())
			}
			//排除相同重复事件
			for _, v := range userEvents {
				tid_subId := fmt.Sprintf("%v_%v_%v_%v", v.GetTaskId(), v.GetSubTaskId())
				//id, err := s.Store.Task().GetTaskInfoByTaskID(cast.ToInt64(v.GetTaskId()))
				task := s.queryTask(cast.ToInt32(v.GetTaskId()), v.GetSubTaskId())
				if task == nil {
					s.log("scheduleLoadingUserEvents", "queryTask", " error task nil,taskid:%v,subtaskId:%v", v.GetTaskId(), v.GetSubTaskId())
					continue
				}
				planInfo, err := unmarshalTaskPlanInfo(task)
				if err != nil {
					s.log("scheduleLoadingUserEvents", "unmarshalTaskPlanInfo", " error task nil,taskid:%v,subtaskId:%v", v.GetTaskId(), v.GetSubTaskId())
					continue
				}
				batchTotal := len(planInfo.GetBatchs())
				numstring := v.GetBatchNum()
				if len(numstring) < 1 {
					tmp := ""
					for i := 0; i < batchTotal; i++ {
						tmp = tmp + "," + fmt.Sprint(i+1)
					}
					numstring = tmp
				}
				bns := strings.Split(numstring, ",")
				for i := 0; i < len(bns); i++ {
					bn := cast.ToInt32(bns[i]) - 1
					bn_ac := fmt.Sprintf("%v_%v", bn, v.GetActionType())
					key := tid_subId + "_" + bn_ac
					if operationId, ok := s.holdUserEventForCacheMap[key]; ok {
						s.log("scheduleLoadingUserEvents", "userevent exists waiting...", " event :%v", key)
					} else {
						s.holdUserEventForCacheMap[key] = fmt.Sprint(v.GetId())
						tid_subId_bn := fmt.Sprintf("%v_%v", tid_subId, bn)
						if userEventChan, okk := s.holdUserEventChanToBatchMap.Load(tid_subId_bn); okk {
							userEventChan.(chan string) <- bn_ac + "_" + operationId
							fsm, _ := s.holdFsm.Load(tid_subId_bn)
							t, _ := s.holdMemTaskDoingMap.Load(tid_subId)
							//启动批次级状态机
							matching := fsm.(*StateMachine).FindTransMatching(t.(*taskDealingData).memTask.GetStatus(), AutoSuccessEvent)
							if matching == nil {
								s.log("mainDeploy", "FSM no rules", " task:%v,status:%v", tid_subId_bn, t.(*taskDealingData).memTask.GetStatus())
							} else {
								err = fsm.(*StateMachine).Trigger(t.(*taskDealingData).memTask.GetStatus(), AutoSuccessEvent, t, bn, matching.From, matching.To)
							}
						} else {
							uec := make(chan string, 1)
							s.holdUserEventChanToBatchMap.Store(tid_subId_bn, uec)
						}
					}
				}
			}
			//for k, v := range s.userEventChanToTaskMap. {
			//	s.userEventsMap.Store(k, v)
			//}
		}
	}()
	return nil
}
