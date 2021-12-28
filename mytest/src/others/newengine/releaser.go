package newfsmengine

import (
	"fmt"
	"mytest.com/src/common/errors"
	"mytest.com/src/common/log"
	"mytest.com/src/net/stke"
	"mytest.com/src/net/zhiyun"
	pb "mytest.com/src/proto"
	"git.code.oa.com/trpc-go/trpc-go/errs"
	"strconv"
	"strings"
)

// releaseInStke 发布在STKE平台
func (s *releaseEngineServiceImpl) releaseInStke(task *pb.Task, target *pb.ReleaseTarget) error {

	service, err := s.Store.Service().QueryServiceBySvcID(task.SvcId)
	if err != nil {
		err = errs.New(errors.QueryServiceBySvcIDError, err.Error())
		log.Error("releaseInStke query service by svc_id error: ", err.Error())
		return err
	}

	plan, err := s.Store.Plan().QueryPlanByID(task.PlanId)
	if err != nil {
		err = errs.New(errors.QueryPlanByIDError, err.Error())
		log.Error("releaseInStke query plan by id error: ", err.Error())
		return err
	}

	application, err := s.Store.Application().QueryNewestApplicationBySvcId(task.SvcId)
	if err != nil {
		err = errs.New(errors.QueryNewestApplicationBySvcIdError, err.Error())
		log.Error("releaseInStke query newest application by svc_id error: ", err.Error())
		return err
	}

	namespaceStrs := strings.Split(target.Namespace, "-") // 从 namespace 获取 环境类型，如 production/test
	workLoad := stke.WorkLoad{
		Namespace:           target.Namespace,
		Env:                 namespaceStrs[len(namespaceStrs)-1],
		ClusterId:           target.Cluster,
		ProjectName:         service.BusinessId,
		WorkloadName:        target.WorkloadName,
		Image:               task.Image,
		CPU:                 strconv.FormatFloat(float64(plan.CpuSize), 'f', 1, 32),
		Memory:              fmt.Sprintf("%dMi", plan.MemSize),
		Replicas:            target.InstanceNum,
		ImagePullSecretName: "csighub-shirewang",
	}
	statefulSet := stke.GetDefaultStatefulSetPlusReq(workLoad)
	res, err := s.Net.StkeSvc().CreateWorkload(statefulSet, application.WorkloadType)
	if err != nil {
		err = errs.New(errors.StkeCreateWorkloadError, err.Error())
		log.Error("stke create workload error: ", err.Error())
		return err
	}

	if res.TkeHttpStatusCode != int(errors.HttpStatusCreate) {
		err = errs.New(errors.StkeCreateWorkloadError, res.Message)
		log.Error("stke create workload error: ", err.Error())
		return err
	}

	return nil
}

// stkeInstanceInfo stke实例的相关信息
type stkeInstanceInfo struct {
	image         string // 当前使用的镜像
	replicas      int32  // 配置文件中的副本数
	readyReplicas int32  // 实际可用的副本数
}

// getStkeInstanceInfo 获取Stke上的实例信息，如实例数、镜像等  readyReplicas 实际已就绪的副本数
func (s *releaseEngineServiceImpl) getStkeInstanceInfo(task *pb.Task, target *pb.ReleaseTarget) (*stkeInstanceInfo, error) {
	application, err := s.Store.Application().QueryNewestApplicationBySvcId(task.SvcId)
	if err != nil {
		err = errs.New(errors.QueryNewestApplicationBySvcIdError, err.Error())
		log.Error("getStkeInstanceInfo query newest application by svc_id error: ", err.Error())
		return nil, err
	}

	// 获取实例信息
	res, err := s.Net.StkeSvc().GetWorkloadInfo(target.Namespace, target.Cluster, application.WorkloadType, target.WorkloadName)
	if err != nil {
		err = errs.New(errors.StkeGetWorkloadInfoError, err.Error())
		log.Error("stke get workload info error: ", err.Error())
		return nil, err
	}

	info := &stkeInstanceInfo{
		image:         res.Spec.Template.Spec.Containers[0].Image,
		replicas:      int32(res.Status.Replicas),
		readyReplicas: int32(res.Status.ReadyReplicas),
	}

	return info, nil
}

// updateWorkloadInStke 更新Stke上的服务实例，无需更新镜像则image传""，无需更新副本数则replicas传-1
func (s *releaseEngineServiceImpl) updateWorkloadInStke(task *pb.Task, target *pb.ReleaseTarget) error {
	application, err := s.Store.Application().QueryNewestApplicationBySvcId(task.SvcId)
	if err != nil {
		err = errs.New(errors.QueryNewestApplicationBySvcIdError, err.Error())
		log.Error("updateWorkloadInStke query newest application by svc_id error: ", err.Error())
		return err
	}

	// 获取当前服务实例的信息
	workloadInfo, err := s.Net.StkeSvc().GetWorkloadInfo(target.Namespace, target.Cluster, application.WorkloadType, target.WorkloadName)
	if err != nil {
		err = errs.New(errors.StkeGetWorkloadInfoError, err.Error())
		log.Error("stke get workload info error: ", err.Error())
		return err
	}

	// 更新服务实例配置中的镜像、副本数等信息
	workloadInfo.Spec.Template.Spec.Containers[0].Image = task.Image
	workloadInfo.Spec.Replicas = target.InstanceNum

	updateWorkloadReq := stke.WorkloadReq{
		Kind:       workloadInfo.Kind,
		APIVersion: workloadInfo.APIVersion,
		Metadata:   workloadInfo.Metadata,
		Spec:       workloadInfo.Spec,
	}
	updatedWorkloadInfo, err := s.Net.StkeSvc().UpdateWorkload(updateWorkloadReq, target.Namespace, target.Cluster, application.WorkloadType)
	if err != nil {
		err = errs.New(errors.StkeUpdateWorkloadError, err.Error())
		log.Error("stke update workload error: ", err.Error())
		return err
	}

	if updatedWorkloadInfo.TkeHttpStatusCode != int(errors.HttpStatusCreate) {
		err = errs.New(errors.StkeUpdateWorkloadError, updatedWorkloadInfo.Message)
		log.Error("stke update workload error: ", err.Error())
		return err
	}

	return nil
}

// releaseInZhiyun 发布在织云平台
func (s *releaseEngineServiceImpl) releaseInZhiyun(task *pb.Task, target *pb.ReleaseTarget) error {

	service, err := s.Store.Service().QueryServiceBySvcID(task.SvcId)
	if err != nil {
		err = errs.New(errors.QueryServiceBySvcIDError, err.Error())
		log.Error("releaseInZhiyun query service by svc_id error: ", err.Error())
		return err
	}

	updateAsyncEXReq := zhiyun.UpdateAsyncEXReq{
		Operator: "shirewang",
		Para: zhiyun.UpdateEXPara{
			Product:   service.BusinessName, // 业务名 如IM_DB
			Name:      service.SvcName,      // 包名 如tdb_freq_agent
			ToVersion: target.NewVersion,    // 所有目标机器升级至同一个版本,升级至版本 如1.0.16
			IPs:       []string{target.Ip},  // 每个target一个ip
		},
	}
	res, err := s.Net.ZhiYunSvc().UpdateAsyncEX(updateAsyncEXReq) // 异步升级包 织云上不部署新服务包，只对现有服务包升级
	if err != nil {
		err = errs.New(errors.ZhiyunUpdateAsyncEXError, err.Error())
		log.Error("zhiyun update async ex error: ", err.Error())
		return err
	}

	if res.Code != int(errors.NormalCode) {
		err = errs.New(errors.ZhiyunUpdateAsyncEXError, res.Msg)
		log.Error("zhiyun update async ex error: ", err.Error())
		return err
	}

	// 将织云实例id回写到target的machine字段中
	target.Machine = res.Data.InstanceId[0]

	return nil
}

// rollbackInZhiyun 在织云平台上回滚
func (s *releaseEngineServiceImpl) rollbackInZhiyun(task *pb.Task, targets []*pb.ReleaseTarget) error {

	service, err := s.Store.Service().QueryServiceBySvcID(task.SvcId)
	if err != nil {
		err = errs.New(errors.QueryServiceBySvcIDError, err.Error())
		log.Error("rollbackInZhiyun query service by svc_id error: ", err.Error())
		return err
	}

	var ips []string
	for _, target := range targets {
		ips = append(ips, target.Ip)
	}

	rollbackEXReq := zhiyun.RollbackEXReq{
		Operator: "shirewang",
		Para: zhiyun.RollbackEXPara{
			IPs:        ips,
			Product:    service.BusinessName,
			Name:       service.SvcName,
			CurVersion: targets[0].PkgVersion,
		},
	}
	res, err := s.Net.ZhiYunSvc().RollbackEX(rollbackEXReq) // 织云安装包回滚
	if err != nil {
		err = errs.New(errors.ZhiyunRollbackEX, err.Error())
		log.Error("zhiyun rollback ex error: ", err.Error())
		return err
	}

	if res.Code != int(errors.NormalCode) {
		err = errs.New(errors.ZhiyunRollbackEX, res.Msg)
		log.Error("zhiyun rollback ex error: ", err.Error())
		return err
	}

	return nil
}

// zhiyunInstanceInfo 织云实例安装的相关信息
type zhiyunInstanceInfo struct {
	instanceId     string
	packagePath    string
	packageVersion string
	installPath    string
	port           string
}

// getInstanceRecordFromZhiyun 获取织云包的安装记录
func (s *releaseEngineServiceImpl) getInstanceRecordFromZhiyun(target *pb.ReleaseTarget) (*zhiyunInstanceInfo, error) {

	getInstallationRecordReq := zhiyun.GetInstallationRecordReq{
		Operator: "shirewang",
		Para: struct {
			IPs  []string `json:"ips"`
			Name string   `json:"name"`
		}{
			IPs:  []string{target.Ip},
			Name: target.SvcName,
		},
	}

	res, err := s.Net.ZhiYunSvc().GetInstallationRecordReq(getInstallationRecordReq)
	if err != nil {
		err = errs.New(errors.ZhiyunGetInstallationRecordReqError, err.Error())
		log.Error("zhiyun get instance record error: ", err.Error())
		return nil, err
	}

	if res.Code != int(errors.NormalCode) {
		err = errs.New(errors.ZhiyunGetInstallationRecordReqError, res.Msg)
		log.Error("zhiyun get instance record error: ", err.Error())
		return nil, err
	}

	info := &zhiyunInstanceInfo{
		instanceId:     res.Data.InstanceInfo[0].InstanceId,
		packagePath:    res.Data.InstanceInfo[0].PackagePath,
		packageVersion: res.Data.InstanceInfo[0].PackageVersion,
		installPath:    res.Data.InstanceInfo[0].InstallPath,
		port:           res.Data.InstanceInfo[0].Port,
	}
	return info, nil
}

// getInstanceStatusFromZhiyun 获取织云包的安装状态(安装、升级、重启等) 返回值：0-进行中，1-成功，其他-失败
func (s *releaseEngineServiceImpl) getInstanceStatusFromZhiyun(target *pb.ReleaseTarget) (int32, error) {

	instanceId, _ := strconv.ParseInt(target.Machine, 10, 64)
	getInstanceInfoReq := zhiyun.GetInstanceInfoReq{
		Operator: "shirewang",
		Para: struct {
			InstanceId []int64 `json:"instanceId"`
		}{
			InstanceId: []int64{instanceId},
		},
	}

	res, err := s.Net.ZhiYunSvc().GetInstanceInfoReq(getInstanceInfoReq)
	if err != nil {
		err = errs.New(errors.ZhiyunGetInstanceInfoReqError, err.Error())
		log.Error("zhiyun get instance info error: ", err.Error())
		return -1, err
	}

	if res.Code != int(errors.NormalCode) {
		err = errs.New(errors.ZhiyunGetInstallationRecordReqError, res.Msg)
		log.Error("zhiyun get instance info error: ", err.Error())
		return -1, err
	}

	return int32(res.Data.InstanceIdResult[0].Status), nil
}

// releaseInPcg123 发布在123平台 QQCD一期Demo先不管123
/*func releaseInPcg123(net net.Net, service *pb.ServiceInfoListData, task *pb.Task, targets []*pb.ReleaseTarget,
	plan *pb.Plan, application *pb.QueryApplicationListData) error {
	// 1。 判断是否存储

	res, err := net.PCG123Svc().IsModuleExisted(service.BizId, service.BusinessId, service.ApplicationName, service.SvcName)
	isExist := res.Get("data").Get("existInfo").Get("bExisted").MustBool()

	// 一个服务可以部署在多个区域的机器上
	var moduleResInfoList []pcg123.ModuleResInfo
	{
	}
	for _, target := range targets {
		moduleResInfo := pcg123.ModuleResInfo{
			Env:     task.ReleaseEnv,
			App:     service.ApplicationName,
			Server:  service.SvcName,
			City:    target.Region,
			CpuSize: plan.CpuSize, // 每个区域都是相同的CPU和Mem？
			MemSize: plan.MemSize,
		}
		moduleResInfoList = append(moduleResInfoList, moduleResInfo)
	}

	// 2。 新建

	if !isExist {
		newCreateModuleReq := pcg123.NewModuleReq{
			Env:               task.ReleaseEnv, // 环境id
			App:               service.ApplicationName,
			Server:            service.SvcName,
			ModuleResInfoList: moduleResInfoList,
			// ServiceInfoListData.Module 模块是级联格式以xx_xx_xx这样的形式保存
			ModuleBaseInfo: pcg123.ModuleBaseInfo{
				App:         service.ApplicationName,
				Server:      service.SvcName,
				CmdbId:      "",
				BizSetId:    "", // 业务集ID，如"PCG_平台治理"
				BizId:       service.BizId,
				Developer:   service.Developer,
				Maintainer:  service.Operations,
				AgentModule: false,
				ServerType:  service.Language,      //服务类型??? （当frameType为trpc时，取值为trpc_go、trpc_java、trpc_cpp、trpc_nodejs、trpc_python和trpc_rust
				BizSetName:  "",                    // "PCG_平台治理"
				BizName:     "",                    // "123平台体验"
				Department:  "",                    // "无线业务系统"
				FrameType:   service.FrameworkType, //框架类型，trpc和other
				DeptId:      "",                    //部门ID， 和department 对应
			},
			EnableIstio: false,
			// "runUser": "mqq"
		}

		net.PCG123Svc().NewCreateModule(newCreateModuleReq)
	}

	// 3。导入

	if isExist {
		queryEnvModuleReq := pcg123.QueryEnvModuleReq{
			App:    service.ApplicationName,
			Server: service.SvcName,
		}

		res, err := net.PCG123Svc().QueryEnvModule(queryEnvModuleReq)
		if err != nil {
			return err
		}

		// 将已存在的模块中的信息替换成数据库中的信息
		envModuleInfo := res.Get("data").Get("envModuleInfoList").GetIndex(0)
		var envVariableList []string
		envVariableListBytes, _ := envModuleInfo.Get("envVariableList").Bytes()
		_ = json.Unmarshal(envVariableListBytes, &envVariableList)

		createEnvModuleReq := pcg123.EnvModuleReq{EnvModule: pcg123.EnvModule{
			Env:                 task.ReleaseEnv,
			App:                 service.ApplicationName,
			Server:              service.SvcName,
			EnvVariableList:     envVariableList,
			ModuleResInfoList:   moduleResInfoList,
			AgentModuleInfoList: []pcg123.AgentModuleInfo{}, // 取已存在模块中的信息
		}}
		net.PCG123Svc().CreateEnvModule(createEnvModuleReq)


		// 3。3 修改trpc配置
		configReq := pcg123.ConfigReq{File: pcg123.ConfigFile{
			ModuleInfo: pcg123.ModuleInfo{
				Env:    task.ReleaseEnv,
				App:    service.ApplicationName,
				Server: service.SvcName,
			},
			Desc:       "",
			Content: application.FrameworkConfig,
			CfgType:    application.ConfigType, // ??? 存的是什么字符串
			UpdateUser: service.Operations,
			UpdateDesc: "update",
		}}
		net.PCG123Svc().CreateOrUpdateConfig(configReq)

	}

	// 4。 扩容，针对每一个区域分别进行扩容
	for _, target := range targets {
		expandInstanceReq := pcg123.InstanceReq{Request: pcg123.InstanceReqBody{
			User:       service.Operations,
			Env:        task.ReleaseEnv,
			App:        service.ApplicationName,
			Server:     service.SvcName,
			Number:     target.InstanceNum,
			Image:      task.Image,
			City:       target.Region,
		}}

		net.PCG123Svc().ExpandInstance(expandInstanceReq)

	}

	return nil
}
*/
