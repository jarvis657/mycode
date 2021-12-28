package errors

const (
	// 请求入口统一的正常编码
	NormalCode       = int32(0) // ErrSuccess RPC 调用成功状态码
	NormalMessage    = "success"
	HttpStatusOk     = int32(200)
	HttpStatusCreate = int32(201)

	// service 服务定义的错误码 10000-20000
	RequestParamValidateError          = 10000
	ErrCopyServiceFailed               = 10001 // ErrCopyFailed 使用copy函数失败
	CreateReleasePlanError             = 10002
	UpdateReleasePlanError             = 10003
	UpdateReleasePlanStatusError       = 10004
	CopyReleasePlanError               = 10005
	CheckReleasePlanError              = 10006
	SaveReleaseBatchError              = 10007
	UpdateReleaseBatchStatusError      = 10008
	DeleteReleaseBatchesError          = 10009
	AutoGenerateBatchesError           = 10010
	AutoGenTimeBatchError              = 10011
	AutoGenTimeBatchesError            = 10012
	GenBatchesReleaseTimeError         = 10013
	CheckTimeAllowReleaseError         = 10014
	CheckTimeAllowReleaseFalse         = 10015
	AutoUpdateTypeBatchesError         = 10016
	SaveBatchHistoryError              = 10017
	QueryBatchHistoryError             = 10018
	ImportBatchHistoryError            = 10019
	QueryReleasePlanByTimeError        = 10020
	QueryReleaseOperationError         = 10021
	CreateServiceInfoError             = 10022
	UpdateServiceInfoError             = 10023
	QueryServiceInfoError              = 10024
	QueryApplicationError              = 10025
	CreateApplicationError             = 10026
	UpdateApplicationError             = 10027
	QueryRouteError                    = 10028
	CreateRouteError                   = 10029
	UpdateRouteError                   = 10030
	QuerySerivceInfoBySvcIDError       = 10031
	DeleteServiceInfoError             = 10032
	DeleteApplicationInfoError         = 10033
	DeleteRouteInfoError               = 10034
	ErrGetServiceInfoFailed            = 10101 // ErrGetServiceInfoFailed 获取service表信息失败
	QueryServiceBySvcIDError           = 10102
	QueryNewestApplicationBySvcIdError = 10103

	// plan 计划相关错误  20001~ 20100
	QueryPlanBySvcIDError    = 20001
	SavePlanError            = 20002
	SaveBatchError           = 20003
	QueryPlanByIDError       = 20004
	CanUpdatePlanError       = 20005
	UpdatePlanError          = 20006
	QueryBatchsByPlanIDError = 20007

	// scheduler 调度者相关错误码 20101 ~ 20200
	QuerySchedulersByStatusError    = 20101
	UpdateSchedulerStatusByIdsError = 20102
	SchedulerQueryTaskBatchError    = 20103
	UpdateTaskPlanInfoError         = 20104

	// pcg123 接口相关错误 30001~ 30100
	QueryCmdbBsiDeptListError        = 30001
	QueryCmdbBsiDeptListNilError     = 30002
	ToJsonError                      = 30003
	QueryCmdBusinessListError        = 30004
	QueryCmdBusinessListNilError     = 30005
	TimestampProtoError              = 30006
	QueryPcg123EnvModuleInfoError    = 30007
	QueryPcg123EnvModuleInfoNilError = 30008
	CountCityInstanceNumError        = 30009
	GetProjectsByUserError           = 30010
	GetProjectClusterError           = 30011
	GetWorkloadsError                = 30012
	ProjectIDNotAuthError            = 30013

	TaskPauseReleaseError          = 40001
	TaskContinueReleaseError       = 40002
	UpdateTaskBatchReleaseWayError = 40003
	ReleaseRollBackError           = 40004
	QueryTaskByTaskIDError         = 40005
	QueryTaskListError             = 40006
	GetTaskInfoByIDError           = 40007
	UpdateApproveStatusError       = 40008
	UpdateTaskImageError           = 40009
	StartReleaseError              = 40010
	UpdateTaskStatusError          = 40011

	// 发布相关错误码
	// Stke 相关错误码 50001 ~ 50100
	StkeCreateWorkloadError  = 50001
	StkeGetWorkloadInfoError = 50002
	StkeUpdateWorkloadError  = 50003

	// 织云 相关错误码 50101 ~ 50200
	ZhiyunUpdateAsyncEXError            = 50101
	ZhiyunRollbackEX                    = 50102
	ZhiyunGetInstallationRecordReqError = 50103
	ZhiyunGetInstanceInfoReqError       = 50104
)
