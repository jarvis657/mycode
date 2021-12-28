package newfsmengine

import (
	"mytest.com/src/common/constant"
	"mytest.com/src/common/errors"
	"mytest.com/src/common/log"
	"mytest.com/src/common/utils"
	. "mytest.com/src/newengine/fsmengine"
	pb "mytest.com/src/proto"
	"git.code.oa.com/trpc-go/trpc-go/errs"
	"github.com/jinzhu/gorm"
	"github.com/spf13/cast"
	"sync"
	"time"
)

//type syncMemTask struct {
//	lock            sync.RWMutex
//	//userSetBatchNum []int32
//}

// CheckSchedulersStatus 定时检查调度者的存活状态
func (s *releaseEngineServiceImpl) scheduleCheckSchedulersStatus() error {
	log.Info("method: [CheckSchedulersStatus]")
	ticker := time.NewTicker(constant.SchedulerStatusCheckPeriod)
	go func() {
		for range ticker.C {
			err, tx, schedulers := s.querySchedulers()
			if err != nil {
				err = errs.New(errors.QuerySchedulersByStatusError, err.Error())
				log.Error("check scheduler status error: ", err.Error())
			}
			// 取出太长时间没有续约的调度者，认为这些调度者已经死亡
			deadSchedulerIds := make([]int32, 0, 2)
			aliveSchedulerIds := make([]int32, 0, 2)
			for _, scheduler := range schedulers {
				if utils.CurrentTimestampToInt()-scheduler.RenewTime > constant.SchedulerRenewTimeThreshold {
					deadSchedulerIds = append(deadSchedulerIds, scheduler.Id)
				} else {
					aliveSchedulerIds = make([]int32, 0, 2)
				}
			}
			// 没有死亡的调度者则无需更新
			if len(deadSchedulerIds) == 0 {
				continue
			}
			//TODO 要讲task表中死亡的scheduler修改成存活的scheduler, 后期做scheduler的负载均衡逻辑
			err = s.Store.Scheduler(tx).UpdateSchedulerStatusByIds(deadSchedulerIds, constant.SchedulerStatusDead)
			if err != nil {
				err = errs.New(errors.UpdateSchedulerStatusByIdsError, err.Error())
				log.Error("update scheduler status error: ", err.Error())
			}

			// 没有存活的调度者则无需更新
			if len(aliveSchedulerIds) == 0 {
				continue
			}
			//存活调度者续约
			err = s.Store.Scheduler(tx).UpdateSchedulerRenewTime(aliveSchedulerIds)
			if err != nil {
				err = errs.New(errors.UpdateSchedulerStatusByIdsError, err.Error())
				log.Error("update scheduler status error: ", err.Error())
			}
		}
	}()
	return nil
}

func (s *releaseEngineServiceImpl) querySchedulers() (error, *gorm.DB, []*pb.Scheduler) {
	var err error
	// 批量更新已死亡的调度者的状态
	tx := s.Store.BeginTx()
	defer s.Store.EndTx(tx, &err)

	// 获取当前为存活状态的调度者
	schedulers, err := s.Store.Scheduler(tx).QuerySchedulersByStatus(constant.SchedulerStatusLive)
	return err, tx, schedulers
}

//query(taskid, subtaskid, batchnum []int32)
//{
//{"1":
//{
//"2":{
//"status":1
//"preversion":"aaaa",
//}
//},
//}
//}
//updatetarget: taskid, subtaskid, batchnum, targetid, status, version

//scheduleLoadingTask 调度核心逻辑 拉去未完成的task获取对应的批次执行发布任务
func (s *releaseEngineServiceImpl) scheduleLoadingTask() error {
	s.holdMemTaskDoingMap = &sync.Map{}
	ticker := time.NewTicker(constant.SchedulerLoadingTaskPeriod)
	go func() {
		for range ticker.C {
			tsids := extractDbIds(s)
			var err error
			tasks, err := s.queryTaskBatch(tsids)
			if err != nil {
				s.log("scheduleLoadingTask", "queryTaskBatch", " err:%v", err)
				return
			}
			for _, task := range tasks {
				s.initMemTaskDoing(task)
			}
		}
	}()
	return nil
}

func (s *releaseEngineServiceImpl) initMemTaskDoing(task *pb.Task) {
	taskPlanInfo, _ := unmarshalTaskPlanInfo(task)
	batchs := taskPlanInfo.GetBatchs()
	mem := &memTaskDoing{
		lock: sync.RWMutex{},
		//batchsStatus:      nil,
		//batchFSM:          nil,
		manualBatchNums:   make([]int32, 0),
		autoBatchNums:     make([]int32, 0),
		taskStatus:        constant.UnDeploy,
		deployingBatchMap: make(map[int32]map[int32]int32),
		rollbackingMap:    make(map[int32]map[int32]int32),
		pausedMap:         make(map[int32]map[int32]int32),
		taskDbId:          task.GetId(),
		taskId:            cast.ToInt32(task.GetTaskId()),
		subTaskId:         task.GetSubTaskId(),
		batchCount:        cast.ToInt32(len(batchs)),
		working:           false,
	}
	batchsStatus := make(map[int32]int32, len(batchs))
	batchsFSM := make(map[int32]*StateMachine, len(batchs))
	for _, batch := range batchs {
		if batch.GetReleaseWay() == 0 {
			mem.manualBatchNums = append(mem.manualBatchNums, batch.GetBatchNum())
		} else {
			mem.autoBatchNums = append(mem.autoBatchNums, batch.GetBatchNum())
		}
		targets := batch.GetReleaseTarget()
		if batch.GetReleaseType() == 0 { //正常发布
			if mem.deployingBatchMap[batch.GetBatchNum()] == nil {
				mem.deployingBatchMap[batch.GetBatchNum()] = make(map[int32]int32)
			}
			for _, t := range targets {
				mem.deployingBatchMap[batch.GetBatchNum()][t.GetId()] = t.GetStatus()
				mem.pausedMap[batch.GetBatchNum()][t.GetId()] = t.GetStatus()
			}
		} else if batch.GetReleaseType() == 1 { //回滚发布
			if mem.rollbackingMap[batch.GetBatchNum()] == nil {
				mem.rollbackingMap[batch.GetBatchNum()] = make(map[int32]int32)
			}
			for _, t := range targets {
				mem.rollbackingMap[batch.GetBatchNum()][t.GetId()] = t.GetStatus()
				mem.pausedMap[batch.GetBatchNum()][t.GetId()] = t.GetStatus()
			}
		}
		batchsStatus[batch.GetBatchNum()] = batch.GetStatus()
		batchsFSM[batch.GetBatchNum()] = initFSM(s)
	}
	mem.batchsStatus = batchsStatus
	mem.batchFSM = batchsFSM
	s.holdMemTaskDoingMap.Store(taskSubId(task.GetTaskId(), task.GetSubTaskId()), mem)
}

//scheduleProcessingMemTaskDoing 任务处理逻辑入口
func (s *releaseEngineServiceImpl) scheduleProcessingMemTaskDoing() error {
	ticker := time.NewTicker(constant.SchedulerProcessingTaskPeriod)
	go func() {
		for range ticker.C {
			//map[string]*memTaskDoing
			s.holdMemTaskDoingMap.Range(func(tid_subId, value interface{}) bool {
				s.working(tid_subId.(string), value.(*memTaskDoing))
				return true
			})
		}
	}()
	return nil
}

func (s releaseEngineServiceImpl) working(tidsubId string,memTask *memTaskDoing)  {
	memTask.lock.Lock()
	defer memTask.lock.Unlock()
	if memTask.working  {
		return
	}
	s.log("scheduleProcessingMemTaskDoing", "working", " checking processing %v", tidsubId)
	s.mainDeploy(memTask)
}


