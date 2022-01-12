package conf

import (
	"context"
	"encoding/json"

	"mytest.com/src/common/constant"
	"mytest.com/src/common/factory"
	"git.code.oa.com/trpc-go/trpc-go/config"
	"git.code.oa.com/trpc-go/trpc-go/log"
)

var (
	mysqlConfig    = &MysqlConf{}
	businessConfig = &BusinessConf{}
)

type Conf interface {

	// 数据库相关配置
	MysqlConfig() config.Config

	// 业务相关配置
	BusinessConfig() config.Config
}

// 从BeanFactory直接获取
func Get() Conf {
	return factory.Get(&Supplier{}).(Conf)
}

func watchTable() {
	resp, err := config.Get(constant.RainbowProviderName).Watch(context.Background(), constant.RainbowMysqlKey)
	if err != nil {
		log.Errorf("watch failed. error: %s", err.Error())
	}

	for conf := range resp {
		var mysqlConf MysqlConf
		err = json.Unmarshal([]byte(conf.Value()), &mysqlConf)
		if err != nil {
			log.Errorf("[Unmarshal failed]\t%s", err.Error())
			continue
		}

		mysqlConfig = &mysqlConf
	}
}

func GetMysqlConfig() config.Config {
	return mysqlConfig
}

func GetBusinessConfig() config.Config {
	return businessConfig
}
