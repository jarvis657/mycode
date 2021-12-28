package newfsmengine

import (
	"container/list"
	"encoding/json"
	"fmt"
	"mytest.com/src/common/constant"
	"mytest.com/src/common/utils"
	pb "mytest.com/src/proto"
	"github.com/spf13/cast"
	"sync"
)

//type syncMemTaskPlanInfo struct {
//	lock sync.RWMutex
//	t    *pb.TaskPlanInfo
//}
//type taskDealingData struct {
//	//部署中 或者回滚中数据
//	//TODO 启动一个goroutine定时拉取状态
//	deployingMarkIndexMap sync.Map //[int32][]int32  k:批次 v:发布中的releaseTarget的index
//	platform              int32
//	lock                  sync.RWMutex
//	memTask               *pb.Task
//	memTaskPlanInfo       *syncMemTaskPlanInfo
//	batchCurrentState     []int32
//
//	batchReceiveEvent []string
//	//用户选择批次  key:批次号 value:对应事件是否完成
//	selectBatchNumsDone map[int32]bool
//	//手动批次     key:批次号 value:对应事件是否完成
//	manualBatchNumsDone map[int32]bool
//	//自动批次     key:批次号 value:对应事件是否完成
//	autoBatchNumsDone map[int32]bool
//	//自动批次
//	autoBatchs map[string]*pb.TaskBatch
//	//手动批次
//	manualBatchs map[string]*pb.TaskBatch
//	//批次在某一阶段执行到那一个releaseTarget了
//	batchProccessed map[int32]int32
//	//是否有手动批次
//	hadManualBatch bool
//	//手动是否完毕
//	manualDone bool
//	//自动是否完毕
//	autoDone bool
//}

func (s *releaseEngineServiceImpl) mainDeploy(t *memTaskDoing) {
	key := taskSubId(t.taskId, t.subTaskId)
	//系统拉取开始执行发布
	if t.taskStatus == constant.UnDeploy {
		t.taskStatus = constant.DeployShould
		t.taskId
		s.updateTaskStatus(t.memTask)
		for i := 0; i < len(t.batchReceiveEvent); i++ {
			go func(tdd *taskDealingData, bn int32) {
				tdd.memTask.GetPlanInfo()
				tid_subId_bn := fmt.Sprintf("%v_%v", key, bn)
				fsm, _ := s.holdFsm.Load(tid_subId_bn)
				if fsm == nil {
					return
				}
				tdd.batchReceiveEvent[bn] = AutoSuccessEvent
				//初始化事件channel
				if _, okk := s.holdUserEventChanToBatchMap.Load(tid_subId_bn); !okk {
					uec := make(chan string, 1)
					s.holdUserEventChanToBatchMap.Store(tid_subId_bn, uec)
				}
				//启动批次级状态机
				matching := fsm.(*StateMachine).FindTransMatching(t.memTask.GetStatus(), AutoSuccessEvent)
				if matching == nil {
					s.log("mainDeploy", "FSM no rules", " task:%v,status:%v", tid_subId_bn, t.memTask.GetStatus())
				} else {
					err = fsm.(*StateMachine).Trigger(t.memTask.GetStatus(), AutoSuccessEvent, t, cast.ToInt32(bn))
				}
			}(t, cast.ToInt32(i))
		}
	}
	if err != nil {
		s.log("mainDeploy", "fsmtrigger error", " err:%v", err)
		t.memTask.Status = constant.DeployFailed
		s.flushToDb(t.memTask, t.memTaskPlanInfo.t)
		s.holdMemTaskDoingMap.Delete(s.funcName(t))
		return
	}
	//}
	//先扩容, 对账成功的goroutine返回结果后继续后续操作
	//TODO 关闭此服务时需要这个流程完毕才能关闭
	//扩容失败则发布失败,回写表数据 退出此goroutine
	//解析task中的planinfo 解析批次数据
	//缩容
}

func (s *releaseEngineServiceImpl) manualAutoSplit(userSelectBatchNums []int32, t *taskDealingData) error {
	taskPlanInfo, err := unmarshalTaskPlanInfo(t.memTask)
	t.memTaskPlanInfo = &syncMemTaskPlanInfo{
		lock: sync.RWMutex{},
		t:    taskPlanInfo,
	}
	batchs := taskPlanInfo.GetBatchs()
	selectManualBatchs := make(map[string]*pb.TaskBatch, len(batchs))
	autoBatchs := make(map[string]*pb.TaskBatch, len(batchs))
	var allManualBatchs = make(map[string]*pb.TaskBatch, len(batchs))
	t.batchProccessed = make(map[int32]int32, len(batchs))
	for _, batch := range batchs {
		//发布方式，0-手动，1-自动
		selectContain := false
		for _, num := range userSelectBatchNums {
			if num == batch.GetBatchNum() {
				selectContain = true
				break
			}
		}
		if batch.GetReleaseWay() == 0 && (t.memTask.GetEmergencyRelease() == 1 || batch.GetStratTime() < utils.CurrentTimestampToInt()) {
			if selectContain {
				selectManualBatchs[fmt.Sprint(batch.GetBatchNum())] = batch
			}
			allManualBatchs[fmt.Sprint(batch.GetBatchNum())] = batch
		} else if batch.GetReleaseWay() == 1 && (t.memTask.GetEmergencyRelease() == 1 || batch.GetStratTime() < utils.CurrentTimestampToInt()) {
			autoBatchs[fmt.Sprint(batch.GetBatchNum())] = batch
		}
	}
	t.manualBatchs = allManualBatchs
	t.autoBatchs = autoBatchs
	t.batchReceiveEvent = make([]string, len(batchs))
	t.batchCurrentState = make([]int32, len(batchs))
	t.manualBatchNumsDone = make(map[int32]bool, len(batchs))
	t.autoBatchNumsDone = make(map[int32]bool, len(batchs))
	for bn, b := range allManualBatchs {
		t.hadManualBatch = true
		if checkDone(b.GetStatus()) {
			t.manualBatchNumsDone[cast.ToInt32(bn)] = true
		} else {
			t.manualBatchNumsDone[cast.ToInt32(bn)] = false
			t.manualDone = false
		}
	}
	for bn, b := range autoBatchs {
		if checkDone(b.GetStatus()) {
			t.autoBatchNumsDone[cast.ToInt32(bn)] = true
		} else {
			t.autoBatchNumsDone[cast.ToInt32(bn)] = false
			t.autoDone = false
		}
	}
	platform, err := s.queryPlatform(t.memTask.SvcId)
	t.platform = platform
	return err
}

func unmarshalTaskPlanInfo(task *pb.Task) (*pb.TaskPlanInfo, error) {
	var pbTaskPlanInfo pb.TaskPlanInfo
	taskPlanInfo := task.GetPlanInfo()
	err := json.Unmarshal([]byte(taskPlanInfo), &pbTaskPlanInfo)
	return &pbTaskPlanInfo, err
}

func marshalTaskPlanInfo(taskPlanInfo *pb.TaskPlanInfo) (string, error) {
	marshal, err := json.Marshal(taskPlanInfo)
	return string(marshal), err
}

func (s *releaseEngineServiceImpl) realDeploy(t *taskDealingData, batchnum int32, targets []*pb.ReleaseTarget) error {
	t.memTaskPlanInfo.lock.Lock()
	defer t.memTaskPlanInfo.lock.Unlock()
	taskPlanInfo, _ := unmarshalTaskPlanInfo(t.memTask)
	batchs := taskPlanInfo.GetBatchs()
	releaseTargetIndex := batchs[batchnum].GetReleaseTargetIndex()
	target := targets[releaseTargetIndex]
	var err error
	//部署平台  1-123平台 2-stke平台 3-织云平台
	switch t.platform {
	case 1:
		s.log("realDeploy", "checkplatform", " donot support %v", t.memTask.GetTaskId())
	case 2:
		err = s.releaseInStke(t.memTask, target)
	case 3:
		err = s.releaseInZhiyun(t.memTask, target)
	}
	if err != nil {
		target.Status = constant.DeployFailed
	} else {
		target.Status = constant.Deploying
	}
	target.ReleaseStartTime = utils.CurrentTimestampToInt()

	if d, ok := t.deployingMarkIndexMap.Load(batchnum); ok {
		d.(*list.List).PushBack(releaseTargetIndex)
	} else {
		ll := list.New()
		ll.PushBack(releaseTargetIndex)
		t.deployingMarkIndexMap.Store(batchnum, ll)
	}
	//for i := listHaiCoder.Front(); i != nil; i = i.Next() {
	//	fmt.Println("Element =", i.Value)
	//}

	batchs[batchnum].ReleaseTargetIndex = releaseTargetIndex + 1
	planInfostring, err := marshalTaskPlanInfo(taskPlanInfo)
	s.log("realDeploy", "marshalTaskPlanInfo", " error:%v", err)
	t.memTask.PlanInfo = planInfostring
	return err
}
