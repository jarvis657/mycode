package newfsmengine

import (
	"fmt"
	"github.com/spf13/cast"
	"strings"
	"time"
)

//执行顺序 (离开当前状态动作ExitAction)->进入当前状态动作Action->上步失败处理OnActionFailure->进入目标状态动作EnterAction
//func initFSM(processor *releaseEngineServiceImpl) *StateMachine {
//	delegate := &DefaultDelegate{P: processor}
//	transitions := []Transition{
//		Transition{From: constant.DeployShould, Event: AutoSuccessEvent, To: constant.Deploying, Action: "DeployShouldToTaskDeploying", ExitAction: "", EnterAction: "EnterStateAction"},
//		Transition{From: constant.Deploying, Event: PauseEvent, To: constant.DeployPaused, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
//		Transition{From: constant.DeployPaused, Event: ContinueEvent, To: constant.Deploying, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
//		Transition{From: constant.Deploying, Event: AutoFailEvent, To: constant.DeployFailed, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
//		Transition{From: constant.Deploying, Event: AutoSuccessEvent, To: constant.DeploySuccess, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
//
//		//TODO机器处理
//		//Transition{From: constant.DeployFailed, Event: RollbackEvent, To: constant.RollbackShould, Action: "", ExitAction: "", EnterAction: ""},
//		//Transition{From: constant.DeploySuccess, Event: RollbackEvent, To: constant.RollbackShould, Action: "", ExitAction: "", EnterAction: ""},
//
//		Transition{From: constant.RollbackShould, Event: PauseEvent, To: constant.RollbackPaused, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
//		Transition{From: constant.RollbackPaused, Event: PauseEvent, To: constant.RollbackShould, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
//		Transition{From: constant.RollbackShould, Event: AutoSuccessEvent, To: constant.Rollbacking, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
//		Transition{From: constant.Rollbacking, Event: AutoFailEvent, To: constant.RollbackFailed, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
//		Transition{From: constant.Rollbacking, Event: AutoSuccessEvent, To: constant.RollbackSuccess, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
//	}
//	return NewStateMachine(delegate, transitions...)
//}
//执行顺序 (离开当前状态动作ExitAction)->进入当前状态动作Action->上步失败处理OnActionFailure->进入目标状态动作EnterAction
//DeployShouldToTaskDeploying 真正发布流程
func (s *releaseEngineServiceImpl) DeployShouldToTaskDeploying(t *taskDealingData, batchNum int32, fromState int32, toState int32) (bool, error) {
	//TODO 获取发布delay时间
	s.manualDeploy(t, batchNum, fromState, toState)
	s.autoDeploy(t, batchNum, fromState, toState)
	return true, nil
}

func (s *releaseEngineServiceImpl) manualDeploy(t *taskDealingData, batchnum int32, fromState int32, toState int32) (bool, error) {
	ticker := time.NewTicker(time.Second * 15)
	taskId_subTaskId := fmt.Sprintf("%v_%v", t.memTask.GetTaskId(), t.memTask.GetSubTaskId())
	//TODO 部署并行修改这里
	//TODO 还有线上部署状态可在这里做,当接到新事件后启动一个goroutine去拉取所有发布中的 然后同步状态
	tid_stid_bn := fmt.Sprintf("%v_%v", taskId_subTaskId, batchnum)
	for {
		userEventChan, _ := s.holdUserEventChanToBatchMap.Load(tid_stid_bn)
		select {
		case x := <-ticker.C:
			_, ok := s.deployingMap.Load(tid_stid_bn)
			s.log("DeployShouldToTaskDeploying_manualDeploy", "rerun", "batchnum:%v,task:%v begin:%v", batchnum, tid_stid_bn, x)
			if ok {
				continue
			}
			s.log("DeployShouldToTaskDeploying_manualDeploy", "rerun", "batchnum:%v,task:%v ready:%v", batchnum, tid_stid_bn, x)
			if !t.manualDone {
				batch := t.manualBatchs[fmt.Sprint(batchnum)]
				releaseTargets := batch.GetReleaseTarget()
				s.deployingMap.Store(tid_stid_bn, false)
				err := s.realDeploy(t, batchnum, releaseTargets)
				s.log("DeployShouldToTaskDeploying_manualDeploy", "manualBatchs", "batchnum:%v,taskId:%v,err:%v", batchnum, t.memTask.GetTaskId(), err)
				continue
			} else if !t.autoDone {
				s.log("DeployShouldToTaskDeploying_manualDeploy", "autoBatchs", "batchnum:%v,taskId:%v start deploy ", batchnum, t.memTask.GetTaskId())
				continue
			}
			//s.checkBatchsStatus(batchNums)
			//TODO 此引擎处理是 通过批次状态 回设置 task  用户所设定的所有批次完成后走下面的逻辑 并判定task 是否需要修改状态
		case op, ok := <-userEventChan.(chan string):
			//key:taskId_subtaskid_batchnum  value: chan string  bn_ac + "_" + operationId
			go func(oplog string) {
				split := strings.Split(oplog, "_")
				opid := cast.ToInt32(split[len(split)-1])
				err := s.updateUserOperationDone(opid)
				s.log("manualDeploy", "updateUserOperationDone", " opid:%v err:%v", opid, err)
				//TODO 拿到对应批次 执行 对应状态函数
			}(op)
			if !ok {
				userEventChan = nil
				return true, nil
			}
			//t.receiveEvent = eventData
			//t.currentState = t.memTask.GetStatus()
			return true, nil
		}
		if userEventChan == nil {
			return true, nil
		}
	}
	return true, nil
}
func (s *releaseEngineServiceImpl) autoDeploy(t *taskDealingData, batchNum int32, fromState int32, toState int32) (bool, error) {
	return true, nil
}

//离开一个状态 提前处理些信息
func (s *releaseEngineServiceImpl) LeaveStateAction(fsmData *taskDealingData, batchNum int32, fromState int32, toState int32) {
	//key := fmt.Sprintf("%v_%v", fsmData.syncMemTask.data.GetTaskId(), fsmData.syncMemTask.data.GetSubTaskId())
	//if userEventChan, ok := s.holdUserEventChanToTaskMap.LoadAndDelete(key); ok {
	//	close(userEventChan.(chan string))
	//	if !checkDone(fsmData.currentState) {
	//		uec := make(chan string, 1)
	//		s.holdUserEventChanToTaskMap.Store(key, uec)
	//	} else {
	//		s.holdMemTaskDoingMap.Delete(key)
	//		s.holdFsm.Delete(key)
	//	}
	//}
}

//进入新状态 提前处理些信息
func (s *releaseEngineServiceImpl) EnterStateAction(t *taskDealingData, batchNum int32, fromState int32, toState int32) {
	t.memTaskPlanInfo.lock.RLock()
	defer t.memTaskPlanInfo.lock.RUnlock()
	key := fmt.Sprintf("%v_%v_%v", t.memTask.GetTaskId(), t.memTask.GetSubTaskId(), batchNum)
	//获取fsmData中的批次判断是否完毕 去修改task 状态信息
	if userEventChan, ok := s.holdUserEventChanToBatchMap.Load(key); ok {
		if checkDone(t.batchCurrentState[batchNum]) && t.manualDone && t.autoDone {
			close(userEventChan.(chan string))
			s.holdMemTaskDoingMap.Delete(key)
			s.holdFsm.Delete(key)
		} else {
			s.log("FSM", "EnterStateAction", " key:%v,current_state:%v, selectbatchnum:%v,manualbatchnum:%v,autobatchnum:%v", key, t.batchCurrentState[batchNum], t.selectBatchNumsDone, t.manualBatchNumsDone, t.autoBatchNumsDone)
		}
		//t.manualBatchs[batchNum]
		s.flushToDb(t.memTask, t.memTaskPlanInfo.t)
	}
}
