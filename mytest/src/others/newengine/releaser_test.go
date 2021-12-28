package newfsmengine

import (
	"fmt"
	"mytest.com/src/common/conf"
	"mytest.com/src/net"
	pb "mytest.com/src/proto"
	"mytest.com/src/store"
	"git.code.oa.com/trpc-go/trpc-go/config"
	"github.com/stretchr/testify/assert"
	"testing"
)

var s *releaseEngineServiceImpl

func init() {
	s = &releaseEngineServiceImpl{}

	yamlPath, _ := conf.ResolveConfigFilePath("trpc_go.yaml")
	yamlConf, err := config.DefaultConfigLoader.Load(yamlPath, config.WithCodec("yaml"))
	if err != nil {
		panic("Loading conf err, err:" + err.Error())
	}

	envName := yamlConf.GetString("global.env_name", "env")
	// 初始化配置层
	s.ConfigStore = conf.NewConf(envName, yamlConf)
	// 初始化存储层
	s.Store = store.NewStore(engine.ConfigStore.MysqlConfig())
	// 初始化网络层
	s.Net = net.NewNet()
}

func Test_releaseInStke(t *testing.T) {

	target := &pb.ReleaseTarget{
		Cluster:      "cls-p83rfhn6",
		InstanceNum:  2,
		WorkloadName: "qqcd-test-zucheng-0421",
		Namespace:    "ns-prjtgngr-787172-test",
	}

	task := &pb.Task{
		Id:     1,
		SvcId:  1,
		PlanId: 1,
		Image:  "csighub.tencentyun.com/admin/tlinux2.2-bridge-tcloud-underlay:latest",
	}

	err := s.releaseInStke(task, target)

	assert.Nil(t, err)
}

func Test_releaseInZhiyun(t *testing.T) {

	task := &pb.Task{
		Id:    2,
		SvcId: 2,
	}

	target := &pb.ReleaseTarget{
		Ip:         "10.238.27.80",
		NewVersion: "1.0.18",
	}

	err := s.releaseInZhiyun(task, target)

	fmt.Println(*target)
	assert.Nil(t, err)
}

func Test_rollbackInZhiyun(t *testing.T) {

	task := &pb.Task{
		Id:    2,
		SvcId: 2,
	}

	target := &pb.ReleaseTarget{
		Ip:         "10.238.27.80",
		PkgVersion: "1.0.18",
	}
	err := s.rollbackInZhiyun(task, []*pb.ReleaseTarget{target})

	assert.Nil(t, err)
}

func Test_getInstanceRecordFromZhiyun(t *testing.T) {
	target := &pb.ReleaseTarget{
		Ip:      "10.238.27.80",
		SvcName: "tdb_freq_agent",
	}

	info, err := s.getInstanceRecordFromZhiyun(target)

	assert.Nil(t, err)

	fmt.Println(*info)
}

func Test_getStkeInstanceInfo(t *testing.T) {
	task := &pb.Task{
		SvcId: 1,
	}

	target := &pb.ReleaseTarget{
		Cluster:      "cls-p83rfhn6",
		WorkloadName: "qqcd-test-zucheng-0416",
		Namespace:    "ns-prjtgngr-787172-test",
	}

	res, err := s.getStkeInstanceInfo(task, target)

	assert.Nil(t, err)

	fmt.Println(*res)
}

func Test_updateWorkloadInStke(t *testing.T) {
	task := &pb.Task{
		SvcId: 1,
		Image: "csighub.tencentyun.com/qidian/nginx:latest",
	}

	target := &pb.ReleaseTarget{
		Cluster:      "cls-p83rfhn6",
		WorkloadName: "qqcd-test-zucheng-0421",
		Namespace:    "ns-prjtgngr-787172-test",
		InstanceNum:  3,
	}

	err := s.updateWorkloadInStke(task, target)

	assert.Nil(t, err)
}

func Test_getInstanceStatusFromZhiyun(t *testing.T) {
	target := &pb.ReleaseTarget{
		Machine: "106249478",
	}

	status, err := s.getInstanceStatusFromZhiyun(target)

	assert.Nil(t, err)

	fmt.Println(status)
}
