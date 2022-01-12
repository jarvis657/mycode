package conf

import (
	"fmt"
	"os"
	"path/filepath"

	"mytest.com/src/common/constant"
	"mytest.com/src/common/factory"
	"mytest.com/src/common/utils"
	"git.code.oa.com/trpc-go/trpc-go/config"
)

type Supplier struct {
	mysql    config.Config
	business config.Config
}

func NewConf(envName string, yaml config.Config) Conf {

	conf := &Supplier{}
	initRainbowConfig(yaml)
	conf.mysql = initMysqlConfig(envName, yaml)
	conf.business = initBusinessConfig(envName, yaml)

	// 注册到bean工厂
	factory.Set(conf)
	return conf
}

func (s *Supplier) MysqlConfig() config.Config {
	return s.mysql
}

// 这里只初始化一次
func (s *Supplier) BusinessConfig() config.Config {
	return s.business
}

func (s *Supplier) Name() string {
	return constant.BeanNameConfig
}

// 初始化 tconf 业务配置
func initBusinessConfig(envName string, conf config.Config) config.Config {

	// 开发环境加载本地
	if envName == constant.DevelopmentDevEnv {
		return conf
	}

	// 其他环境加载七彩石配置
	return GetBusinessConfig()
}

// 初始化数据库配置
func initMysqlConfig(envName string, conf config.Config) config.Config {

	// 开发环境加载本地
	if envName == constant.DevelopmentDevEnv {
		return conf
	}

	// 其他环境加载七彩石配置
	return GetMysqlConfig()
}

// 获取配置文件路径
func ResolveConfigFilePath(path string) (string, error) {
	if filepath.IsAbs(path) {
		return path, nil
	}

	if configFile := utils.FindFile(filepath.Join("config", path)); configFile != "" {
		return configFile, nil
	}

	if configFile := utils.FindPath(path, []string{".", "./.."}, func(fileInfo os.FileInfo) bool {
		return !fileInfo.IsDir()
	}); configFile != "" {
		return configFile, nil
	}

	return "", fmt.Errorf("failed to find config file %s", path)
}
