package constant

import "time"

const (

	// 调用接口code返回正常值
	ReturnNormal = 0

	// 请求返回正常
	NormalSuccess = "success"
	NormalFail    = "fail"

	// 读取配置文件默认更换
	DevelopmentDevEnv = "dev"

	// 数据库key
	DBName = "release"

	// 数据字符串为空
	DataStringNull  = "null"
	DataStringEmpty = ""
	DataStringZero  = "0.00"
	ArrowUp         = 1
	ArrowDown       = 0

	// 保留小数点位数
	FloatToStringPrec  = 2
	DefaultLimitMax    = 100
	DefaultPullPageSiz = 10000

	// 非法数字标识
	IllegalNumString = "NaN"
	TimeFormatLayout = "2006-01-02"

	// 离线接口请求时间，时间适当延长, 单位s
	ClientExtendTime = 10

	// tof接口查询假期
	ToFQueryHoliday = "/api/v1/Holiday/CheckHolidayDefine"

	// bean工厂名字注册
	BeanNameNet    = "net"
	BeanNameStore  = "store"
	BeanNameConfig = "config"

	// 计划相关和批次相关状态
	PlanBatchUnRelease = 0
	PlanBatchDeleted   = 1

	// 调度者续约时间阈值，超过则认为该调度者已死亡，单位秒
	SchedulerRenewTimeThreshold = 60

	// 调度者存活状态检查周期，单位秒
	SchedulerStatusCheckPeriod = time.Second * 30

	// 调度者拉取任务周期，单位秒
	SchedulerLoadingTaskPeriod = time.Second * 10

	//调度者拉取用户操作记录周期,单位秒
	SchedulerLoadingUserEventsPeriod = time.Second * 3

	// 调度者执行任务周期，单位秒
	SchedulerProcessingTaskPeriod = time.Second * 1

	// 调度者执行发布task，单位秒
	SchedulerRunDeployPeriod = time.Second * 1

	//调度引擎版本 大版本_小版本
	SchedulerParamVersion = 100_01
	// 调度者存活状态
	SchedulerStatusLive = 0
	SchedulerStatusDead = 1

	//批次 可以发布
	CanDeploy = 1
	//批次 不能发布
	CanotDeploy = 0

	//skte对应创建类型
	StateFulSetPluses = "statefulsetpluses"
	StateFulSets      = "statefulsets"
	Deployments       = "deployments"

	//织云配置
	ZhiyunPWDKey = "zy_pwd"
	ZhiyunCaller = "new_zhiyun_caller_devops_release"

	// 七彩石配置
	RainbowProviderName    = "rainbow"
	RainbowMysqlKey        = "mysql_config"
	RainbowBusinessKey     = "business_config"
	RainbowConnectStr      = "http://api.rainbow.oa.com:8080"
	RainbowDefaultAPPIDStr = "c2b29253-9057-44ac-8cdc-6c8b82611f94"
	RainbowDefaultGroupStr = "qqcd_test_service"
	RainbowStkePubKey      = "stke_pub_key"
	RainbowStkePrivKey     = "sake_priv_key"

	// 星云接口配置
	XingYunAppkey = "release_monitor"
	XingYunToken  = "release_monitor@GW9Jd"

	//123对应API
	ModuleExistedURL        string = "http://api.apigw.oa.com/Admin/admin/isModuleExisted"
	QueryEnvModuleURL       string = "http://api.apigw.oa.com/Admin/admin/queryEnvModuleURL"
	NewCreateModuleURL      string = "http://api.apigw.oa.com/Admin/admin/NewCreateModule"
	CreateEnvModuleURL      string = "http://api.apigw.oa.com/Admin/admin/createEnvModule"
	CreateOrUpdateConfigURL string = "http://api.apigw.oa.com/Admin/config/createOrUpdateConfig"
	ExpandInstanceURL       string = "http://api.apigw.oa.com/Admin/task/expandInstance"
	PublishInstanceURL      string = "http://api.apigw.oa.com/Admin/task/publishInstance"
	GetInstanceInfoURL      string = "http://api.apigw.oa.com/Admin/admin/getInstanceInfo"
	QueryTaskInfoURL        string = "http://api.apigw.oa.com/Admin/task/queryTaskInfo"
	QueryCmdbBsiDeptURL     string = "http://api.apigw.oa.com/pms/get_cmdb_bsi_dept"
	QueryCmdbBusinessURL    string = "http://api.apigw.oa.com/pms/get_cmdb_business"
	QueryPcg123EnvModuleURL string = "http://api.apigw.oa.com/Admin/admin/queryEnvModule"
	PCG123URl               string = "http://api.apigw.oa.com"

	//织云对应API
	ZhiyunAPIURL    string = "http://pkg.isd.com"
	ZhiyunRouterURL string = "http://yun.isd.com"

	//stke对应API
	GetProjectClusterURL = "https://tke.kubernetes.oa.com/v2/project/getProjectCluser"
	CreateWorkloadURL    = "https://tke.kubernetes.oa.com/v2/forward/stke/apis/platform.stke/v1alpha1/namespaces/%s/%s"    //{namespaces}/{type}
	DeleteWorkloadURL    = "https://tke.kubernetes.oa.com/v2/forward/stke/apis/apps/v1/namespaces/%s/%s/%s"                //{namespaces}/{type}/{name}
	GetWorkloadURL       = "https://tke.kubernetes.oa.com/v2/forward/stke/apis/platform.stke/v1alpha1/namespaces/%s/%s/%s" //{namespaces}/{type}/{name}
	WorkloadURL          = "https://tke.kubernetes.oa.com/v2/forward/stke/apis/platform.stke/v1alpha1/namespaces/%s/%s"
	GetProjectsByUserURL = "https://tke.kubernetes.oa.com/api/projects/user/%s"

	//星云接口API
	GetServerByBusiIDURL string = "http://api.xingyun.tencentyun.com/api/server_api/get_server_by_busiId"
)

// 前端用户操作动作相关
const (
	ActionStart     = 1   // 用户点击开始发布动作
	ActionContinuer = 2   // 用户点击继续发布动作
	ActionPause     = 3   // 用户点击暂停动作
	ActionRollBack  = 4   // 用户点击回滚动作
	ActionAbort     = 5   // 用户点击终止动作
	ActionNull      = 100 // 无用户动作为默认值
)

// 审批状态
const (
	UnApprove      = 0 // 未审批
	Approved       = 1 // 通过
	ApproveReject  = 2 // 拒绝
	ApproveOverdue = 3 // 过期
)

// 操作记录
const (
	OperationAddPlanBatch         = "添加计划批次"
	OperationUpdatePlanBatch      = "修改计划批次"
	OperationAddTask              = "添加计划任务"
	OperationBatchPauseRelease    = "暂停批次发布"
	OperationBatchContinueRelease = "继续批次发布"
	OperationBatchRollBackRelease = "回滚批次发布"
	OperationTaskPauseRelease     = "暂停任务发布"
	OperationTaskContinueRelease  = "继续任务发布"
	OperationTaskRollBackRelease  = "回滚任务发布"
)
