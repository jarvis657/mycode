package model

// task表中的plan_info字段信息
type TaskPlanInfo struct {
	Plan   *TaskPlan    `json:"plan"`
	Batchs []*TaskBatch `json:"batchs"`
	Stage  *Stage       `json:"stage"`
}

type TaskPlan struct {
	PlanId           int32   `json:"plan_id"`
	PlanName         string  `json:"plan_name"`          // 计划名称
	SvcId            int32   `json:"svc_id"`             // 关联服务id
	UpgradeType      int32   `json:"upgrade_type"`       // 更新策略 1-滚动更新 2-蓝绿  3-金丝雀
	BatchUpgradeNum  int32   `json:"batch_upgrade_num"`  // 每批次升级多少台机器，容器则为pod数量
	BatchInterval    int32   `json:"batch_interval"`     // 每批次升级时间间隔 单位为秒
	CpuSize          float32 `json:"cpu_size"`           // 需要用到的cpu核数
	MemSize          int32   `json:"mem_size"`           // 需要用到的内存资源大小
	RebootType       int32   `json:"reboot_type"`        // 针对织云字段 重启类型 0-普通重启 1-热重启（针对spp有效）
	RebootInfo       string  `json:"reboot_info"`        // 重启信息json存储 {"process":"all"}
	TotalBatchNum    int32   `json:"total_batch_num"`    // 总批次
	TotalInstanceNum int32   `json:"total_instance_num"` // 总实例总数
	ModuleId         string  `json:"module_id"`          // 模块id信息,这里可能存在多个，db存储逗号分割 针对织云根据该值获取对应的发布机器
	TolerateRatio    int32   `json:"tolerate_ratio"`     // 发布失败容忍百分比
	ReleaseStartTime int32   `json:"release_start_time"` // 回填-> 计划实际开始发布的时间
	RleaseEndTime    int32   `json:"rlease_end_time"`    // 回填-> 计划完成发布的时间
}

type TaskBatch struct {
	PlanId           int32            `json:"plan_id"`            // 计划id
	BatchNum         int32            `json:"batch_num"`          // 对应批次表的中的batch_num
	ReleaseWay       int32            `json:"release_way"`        // 发布方式
	ReleaseType      int32            `json:"release_type"`       // 0=normal，1=rollback  当用户点击回滚的时候 除了未开始的都需要重新生成塞到最后一批 修改原来未发布的机器状态为跳过，批次也跳过
	StratTime        int32            `json:"strat_time"`         // 发布允许开始时间
	EndTime          int32            `json:"end_time"`           // 发布允许结束时间
	ReleaseStartTime int32            `json:"release_start_time"` // 回填-> 批次实际开始发布时间
	ReleaseEndTime   int32            `json:"release_end_time"`   // 回填-> 批次实际完成发布时间
	ReleaseTarget    []*ReleaseTarget `json:"release_target"`     // 发布的目标机器
	Status           int32            `json:"status"`             // 回填-> 批次发布状态 0-未发布 1-发布中 2-发布成功 3-发布失败  4-发布暂停 5-发布回滚 6-发布跳过",
}

type ReleaseTarget struct {
	Ip               string `json:"ip"`                 // 机器的ip信息
	Region           string `json:"region"`             // 区域如上海电信
	BusinessPath     string `json:"business_path"`      // 业务路径如 长连接 - 图片统一存储 - [QQ斗图][逻辑] 织云使用
	Type             string `json:"type"`               // idc 织云使用
	SvcName          string `json:"svc_name"`           // 服务名称
	PkgVersion       string `json:"pkg_version"`        // 回填-> 重新实时拉取当前机器的版本信息或镜像
	PkgName          string `json:"pkg_name"`           //
	NewVersion       string `json:"new_version"`        // 0.0.2
	Machine          string `json:"machine"`            // 机器名 织云使用
	Cluster          string `json:"cluster"`            // 集群信息 stke使用
	InstanceNum      int32  `json:"instance_num"`       // 机器实例 预期需要发布的实例数
	Status           int32  `json:"status"`             // 回填-> 批次发布状态 0-未发布 1-发布中 2-发布成功 3-发布失败  4-发布暂停 5-发布回滚 6-发布跳过",
	Result           string `json:"result"`             // 回填-> 每台机器发布后的状态信息, 机器发布完成后修改该字段
	ReleaseStartTime int32  `json:"release_start_time"` // 回填-> 当前机器开始发布的时间
	ReleaseEndTime   int32  `json:"release_end_time"`   // 回填-> 当前机器结束发布的时间
	WorkloadName     string `json:"workload_name"`      // 工作负载名称 针对stke
	Id               int32  `json:"id"`
	Namespaces       string `json:"namesapces"`
}
type Stage struct {
	Compile          *CompileStage     `json:"compile"`
	TestReleaseStage *TestReleaseStage `json:"test_release_stage"`
	ProReleaseStage  *ProReleaseStage  `json:"pro_release_stage"`
	TestRouteStage   *TestRouteStage   `json:"test_route_stage"`
	RollBackStage    *RollBackStage    `json:"roll_back_stage"`
	Status           int32             `json:"status"` // 取消 成功（全部成功），失败（有失败即失败），等待（未执行默认状态） 针对的是task任务的
}

type CompileStage struct {
	PipelineId  string `json:"pipeline_id"`  // 编译流水线id
	PipelineUrl string `json:"pipeline_url"` // 流水线地址
	Msg         string `json:"msg"`          // 编译的结果 通过流水线id 拉取当前执行的结果
	Status      int32  `json:"status"`       // success failed running pending
}

type TestReleaseStage struct {
	Msg    string `json:"msg"`
	Status int32  `json:"status"` // success failed running pending
}

type ProReleaseStage struct {
	Msg    string `json:"msg"`    // xxxx
	Status int32  `json:"status"` // success failed running pending
}

type TestRouteStage struct {
	Msg    string `json:"msg"`    // xxxx
	Status int32  `json:"status"` // success failed running pending
}

type RollBackStage struct {
	Msg    string `json:"msg"`    // xxxx
	Status int32  `json:"status"` // success failed running pending
}
