package newfsmengine

import (
	pb "mytest.com/src/proto"
	"github.com/spf13/cast"
)

func (s releaseEngineServiceImpl) queryTask(taskId int32, subTaskId int32) *pb.Task {
	var err error
	tx := s.Store.BeginTx()
	defer s.Store.EndTx(tx, &err)
	tasks, err := s.Store.Task(tx).GetTaskInfoByTaskID(cast.ToInt64(taskId))
	for _, task := range tasks {
		if task.GetSubTaskId() == subTaskId {
			return task
		}
	}
	return nil
}

func (s *releaseEngineServiceImpl) queryTaskBatch(tid_sids []int32) ([]*pb.Task, error) {
	var err error
	// 批量更新已死亡的调度者的状态
	tx := s.Store.BeginTx()
	defer s.Store.EndTx(tx, &err)
	// 获取当前为存活状态的调度者
	canRunTasks, err := s.Store.Task(tx).SchedulerQueryTaskBatch(s.schedulerName, s.paramVersion,tid_sids)
	return canRunTasks, err
}

func (s *releaseEngineServiceImpl) updateTaskStatus(taskDbId int32,status int32) {
	var err error
	tx := s.Store.BeginTx()
	defer s.Store.EndTx(tx, &err)
	//TODO 这里要改，因为update 需要唯一
	err = s.Store.Task(tx).UpdateTaskStatus(cast.ToInt64(taskDbId), task.GetStatus())
}

func (s *releaseEngineServiceImpl) updateTask(task *pb.Task) error {
	var err error
	// 批量更新已死亡的调度者的状态
	tx := s.Store.BeginTx()
	defer s.Store.EndTx(tx, &err)
	// 获取当前为存活状态的调度者
	err = s.Store.Task(tx).UpdateTaskStatus(int64(task.GetId()), task.GetStatus())
	return err
}

func (s *releaseEngineServiceImpl) queryPlatform(svcId int32) (int32, error) {
	var err error
	// 批量更新已死亡的调度者的状态
	tx := s.Store.BeginTx()
	defer s.Store.EndTx(tx, &err)
	// 获取当前为存活状态的调度者
	app, err := s.Store.Application(tx).QueryNewestApplicationBySvcId(svcId)
	platform := app.GetType()
	return platform, err
}

func (s *releaseEngineServiceImpl) flushToDb(memTask *pb.Task, memTaskPlanInfo *pb.TaskPlanInfo) {
	var err error
	tx := s.Store.BeginTx()
	defer s.Store.EndTx(tx, &err)
	info, err := marshalTaskPlanInfo(memTaskPlanInfo)
	if err != nil {
		s.log("flusToDb", "marshalTaskPlanInfo", " error %v", info)
	} else {
		err = s.Store.Task(tx).UpdateTaskPlanInfo(memTask.GetId(), memTask.GetSubTaskId(), memTask.GetStatus(), info)
	}

}

//发布逻辑
func (s releaseEngineServiceImpl) oneTargetDeploy(taskDealingData *taskDealingData, releaseTarget *pb.ReleaseTarget) {
	taskDealingData.lock.Lock()
	defer taskDealingData.lock.Unlock()

}

//回滚逻辑
func (s releaseEngineServiceImpl) oneTargetRollback(taskDealingData *taskDealingData, releaseTarget *pb.ReleaseTarget) {
	taskDealingData.lock.Lock()
	defer taskDealingData.lock.Unlock()

}
