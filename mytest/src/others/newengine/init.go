package newfsmengine

import (
	"mytest.com/src/common/conf"
	"mytest.com/src/common/constant"
	"mytest.com/src/common/log"
	"mytest.com/src/common/utils"
	"mytest.com/src/net"
	. "mytest.com/src/newengine/fsmengine"
	pb "mytest.com/src/proto"
	"mytest.com/src/store"
	"git.code.oa.com/trpc-go/trpc-go/config"
	"sync"
	"time"
)

//处理中的任务信息
type memTaskDoing struct {
	lock              sync.RWMutex
	batchsStatus      map[int32]int32
	batchFSM          map[int32]*StateMachine
	taskStatus        int32
	manualBatchNums   []int32
	autoBatchNums    []int32

	deployingBatchMap map[int32]map[int32]int32 //batchnum:[targetId][status]
	rollbackingMap    map[int32]map[int32]int32 //batchnum:[targetId][status]
	pausedMap         map[int32]map[int32]int32 //batchnum:[targetId][status]
	taskDbId          int32
	taskId            int32
	subTaskId         int32
	batchCount        int32
	working           bool //是否有gorutine在执行
}

type releaseEngineServiceImpl struct {
	// 配置interface
	ConfigStore conf.Conf

	// 存储interface
	Store store.Store

	// 网络interface
	Net net.Net

	//启动时设置调度者名称(随机字符串)
	schedulerName string
	//启动时设置调度程序版本号
	paramVersion int32
	//定期拉取数据库中task表的信息 key: taskId_subtaskid value: *memTaskDoing
	holdMemTaskDoingMap *sync.Map // map[string]*memTaskDoing
}

func (s *releaseEngineServiceImpl) initScheduler() error {
	log.Info("method: [initScheduler]")
	var err error
	tx := s.Store.BeginTx()
	defer s.Store.EndTx(tx, &err)
	schdeuler := &pb.Scheduler{
		SchedulerName: utils.GetUUID(),
		RenewTime:     utils.CurrentTimestampToInt(),
		ParamVersion:  constant.SchedulerParamVersion,
	}
	s.schedulerName = schdeuler.SchedulerName
	s.paramVersion = schdeuler.ParamVersion
	_, err = s.Store.Scheduler(tx).CreateScheduler(schdeuler)
	return err
}

var engine *releaseEngineServiceImpl

// 初始化数据中心服务
func init() {
	engine = &releaseEngineServiceImpl{}

	yamlPath, _ := conf.ResolveConfigFilePath("trpc_go.yaml")
	yamlConf, err := config.DefaultConfigLoader.Load(yamlPath, config.WithCodec("yaml"))
	if err != nil {
		panic("Loading conf err, err:" + err.Error())
	}

	envName := yamlConf.GetString("global.env_name", "env")

	// 初始化配置层
	engine.ConfigStore = conf.NewConf(envName, yamlConf)

	// 初始化存储层
	engine.Store = store.NewStore(engine.ConfigStore.MysqlConfig())

	// 初始化网络层
	engine.Net = net.NewNet()
	//本调用者机器存活设置
	if err = engine.initScheduler(); err != nil {
		panic("init alive scheduler err, err:" + err.Error())
	}
	// 启动机器调度存活检测逻辑
	if err = engine.scheduleCheckSchedulersStatus(); err != nil {
		panic("start check scheduler status err, err:" + err.Error())
	}
	// 拉取任务
	if err = engine.scheduleLoadingTask(); err != nil {
		panic("start loading task err, err:" + err.Error())
	}
	// 运行部署任务
	if err = engine.scheduleProcessingMemTaskDoing(); err != nil {
		panic("start loading task err, err:" + err.Error())
	}
	//拉取用户操作事件
	if err = engine.scheduleLoadingUserEvents(); err != nil {
		panic("start loading user events err, err:" + err.Error())
	}
	//TODO 状态机因为无法自旋,需要外部触发所以将状态内的事件转到单独的goroutine来触发事件
	//if err = engine.(); err != nil {
	//	panic("start loading user events err, err:" + err.Error())
	//}
	//TODO 运行对账程序,所有发布中，回滚中的任务处理
	if err = engine.scheduleMemTaskStatus(); err != nil {
		panic("start loading user events err, err:" + err.Error())
	}

}
func (s releaseEngineServiceImpl) log(mainMethod string, point string, format string, args ...interface{}) {
	f := "host:%v paramversion:%v invoke method: [%v][%v] processing time:%v => "
	f = f + format
	log.Infof(f, s.schedulerName, s.paramVersion, mainMethod, point, time.Now().Format(time.RFC3339), args)
}
