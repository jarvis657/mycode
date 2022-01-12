package conf

import (
	"encoding/json"
	"sync"

	"mytest.com/src/common/constant"
	"git.code.oa.com/rainbow/golang-sdk/confapi"
	"git.code.oa.com/rainbow/golang-sdk/config"
	"git.code.oa.com/rainbow/golang-sdk/types"
	"git.code.oa.com/rainbow/golang-sdk/watch"
	"git.code.oa.com/trpc-go/trpc-go/log"

	trpcConfig "git.code.oa.com/trpc-go/trpc-go/config"
)

var (
	confInitOnce sync.Once
)

// watchCallBack watch call back
func watchCallBack(oldVal watch.Result, newVal []*config.KeyValueItem) error {
	for _, val := range newVal {
		if keyValue := getMysqlKeyValue(val.KeyValues); keyValue != "" {
			var staticConfig MysqlConf
			err := json.Unmarshal([]byte(keyValue), &staticConfig)
			if err != nil {
				return err
			}
			mysqlConfig = &staticConfig
		}
	}
	return nil
}

func getMysqlKeyValue(keyValues []*config.KeyValue) string {
	for _, keyValue := range keyValues {
		if keyValue != nil {
			if keyValue.Key == constant.RainbowMysqlKey {
				return keyValue.Value
			}
		}
	}
	return ""
}

func initRainbowConfig(yamlConf trpcConfig.Config) {
	confInitOnce.Do(func() {
		err := getRainbowConfig(yamlConf)
		if err != nil {
			panic(err)
		}
	})
}

func getRainbowConfig(yamlConf trpcConfig.Config) error {
	envName := yamlConf.GetString("config.rainbow.providers.env_name", "Default")
	appID := yamlConf.GetString("config.rainbow.providers.appid", constant.RainbowDefaultAPPIDStr)
	groups := yamlConf.GetString("config.rainbow.providers.group", constant.RainbowDefaultGroupStr)
	rainbow, err := confapi.New(
		types.ConnectStr(constant.RainbowConnectStr),
		types.IsUsingLocalCache(true),
		types.IsUsingFileCache(true),
		types.EnvName(envName),
		types.AppID(appID),
		types.Groups(groups),
	)
	if err != nil {
		log.Errorf("init rainbow config failed: %s", err.Error())
		return err
	}

	getOpts := make([]types.AssignGetOption, 0)
	getOpts = append(getOpts, types.WithAppID(appID))
	getOpts = append(getOpts, types.WithGroup(groups))
	val, err := rainbow.Get(constant.RainbowMysqlKey, getOpts...)
	if err != nil {
		log.Errorf("get config failed: %s", err.Error())
		return err
	}

	var staticConfig MysqlConf
	err = json.Unmarshal([]byte(val), &staticConfig)
	if err != nil {
		log.Errorf("get config failed: %s", err.Error())
		return err
	}
	mysqlConfig = &staticConfig

	//to do 增加业务配置初始化
	configMap := make(map[string]interface{})
	for _, key := range BusinessKeys {
		tempVal, err := rainbow.Get(key, getOpts...)
		if err != nil {
			log.Errorf("get config failed: %s", err.Error())
			return err
		}
		configMap[key] = tempVal
	}
	businessConfig.ConfigMap = configMap
	return nil
}
