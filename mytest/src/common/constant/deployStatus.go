package constant

const (
	UnDeploy       = 0 //-未发布
	DeployShould   = 1 //-待发布
	Deploying      = 2 //-发布中
	DeployPaused   = 3 //-发布已暂停
	DeployFailed   = 4 //-发布失败
	DeploySuccess  = 5 //-发布成功
	RollbackShould = 6 //-待回滚
	RollbackPaused = 7 //-回滚暂停
	Rollbacking    = 9 //-回滚中

	RollbackFailed  = 10  //-回滚失败
	RollbackSuccess = 11 //-回滚成功
	Pass            = 12 //-发布跳过
	TaskDelete      = 20 //-任务为删除状态
)
