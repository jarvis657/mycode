package conf

import (
	"context"
	"encoding/json"
	"fmt"
	"reflect"
	"strings"
	"time"

	"mytest.com/src/common/constant"
	"git.code.oa.com/trpc-go/trpc-go/config"
	"github.com/spf13/cast"
)

// MysqlConf mysql对应需要的配置项
type MysqlConf struct {
	DBName       string `json:"dbname"`
	Host         string `json:"host"`
	Port         int    `json:"port"`
	Username     string `json:"username"`
	Password     string `json:"password"`
	Timeout      string `json:"timeout"`
	MaxIdleConns int    `json:"max_idle_conns"`
	MaxOpenConns int    `json:"max_open_conns"`
}

// Load 从七彩石加载配置
func (c *MysqlConf) Load() error {
	ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
	defer cancel()
	conf, err := config.Get(constant.RainbowProviderName).Get(ctx, constant.RainbowMysqlKey)
	if err != nil {
		return err
	}

	err = json.Unmarshal([]byte(conf.Value()), c)
	if err != nil {
		return err
	}
	return nil
}

// Reload 重加载配置值
func (c *MysqlConf) Reload() {
	c.Load()
}

// GetString 根据key获取对应字段的string值，找不到key返回默认值
func (c *MysqlConf) GetString(key string, defaultValue string) string {
	if value, err := c.find(key); err == nil {

		if result, ok := value.(string); ok {
			return result
		}

		if result, err := cast.ToStringE(value); err == nil {
			return result
		}
	}

	return defaultValue
}

// Unmarshal 将MysqlConf反序列化为对应输入的结构体
func (c *MysqlConf) Unmarshal(in interface{}) error {
	return json.Unmarshal(c.Bytes(), in)
}

// Get 根据key获取对应字段的值，找不到key返回默认值
func (c *MysqlConf) Get(key string, defaultValue interface{}) interface{} {
	if v, err := c.find(key); err == nil {
		return v
	}

	return defaultValue
}

// GetInt 根据key读取int类型配置，找不到key返回默认值
func (c *MysqlConf) GetInt(key string, defaultValue int) int {
	return cast.ToInt(c.findWithDefaultValue(key, defaultValue))
}

// GetInt32 GetInt
func (c *MysqlConf) GetInt32(key string, defaultValue int32) int32 {
	return cast.ToInt32(c.findWithDefaultValue(key, defaultValue))
}

// GetInt64 根据key读取int64类型配置
func (c *MysqlConf) GetInt64(key string, defaultValue int64) int64 {
	return cast.ToInt64(c.findWithDefaultValue(key, defaultValue))
}

// GetUint 根据key读取int类型配置
func (c *MysqlConf) GetUint(key string, defaultValue uint) uint {
	return cast.ToUint(c.findWithDefaultValue(key, defaultValue))
}

// GetUint32 根据key读取uint32类型配置
func (c *MysqlConf) GetUint32(key string, defaultValue uint32) uint32 {
	return cast.ToUint32(c.findWithDefaultValue(key, defaultValue))
}

// GetUint64 根据key读取uint64类型配置
func (c *MysqlConf) GetUint64(key string, defaultValue uint64) uint64 {
	return cast.ToUint64(c.findWithDefaultValue(key, defaultValue))
}

// GetFloat64 根据key读取float64类型配置
func (c *MysqlConf) GetFloat64(key string, defaultValue float64) float64 {
	return cast.ToFloat64(c.findWithDefaultValue(key, defaultValue))
}

// GetFloat32 根据key读取float32类型配置
func (c *MysqlConf) GetFloat32(key string, defaultValue float32) float32 {
	return cast.ToFloat32(c.findWithDefaultValue(key, defaultValue))
}

// GetBool 根据key读取bool类型配置
func (c *MysqlConf) GetBool(key string, defaultValue bool) bool {
	return cast.ToBool(c.findWithDefaultValue(key, defaultValue))
}

func (c *MysqlConf) findWithDefaultValue(key string, defaultValue interface{}) interface{} {
	v, err := c.find(key)
	if err != nil {
		return defaultValue
	}

	switch defaultValue.(type) {
	case bool:
		v, err = cast.ToBoolE(v)
	case string:
		v, err = cast.ToStringE(v)
	case int:
		v, err = cast.ToIntE(v)
	case int32:
		v, err = cast.ToInt32E(v)
	case int64:
		v, err = cast.ToInt64E(v)
	case uint:
		v, err = cast.ToUintE(v)
	case uint32:
		v, err = cast.ToUint32E(v)
	case uint64:
		v, err = cast.ToUint64E(v)
	case float64:
		v, err = cast.ToFloat64E(v)
	case float32:
		v, err = cast.ToFloat32E(v)
	default:
	}

	if err != nil {
		return defaultValue
	}
	return v
}

func (c *MysqlConf) find(key string) (interface{}, error) {
	mysqlKey := parseKey(key)
	val := reflect.ValueOf(*c)
	for i := 0; i < val.Type().NumField(); i++ {
		jsonTag := val.Type().Field(i).Tag.Get("json")
		if mysqlKey == jsonTag {
			return val.Field(i).Interface(), nil
		}
	}
	return nil, fmt.Errorf("")
}

// IsSet 根据key判断是否在结构体中
func (c *MysqlConf) IsSet(key string) bool {
	mysqlKey := parseKey(key)
	val := reflect.ValueOf(*c)
	for i := 0; i < val.Type().NumField(); i++ {
		jsonTag := val.Type().Field(i).Tag.Get("json")
		if mysqlKey == jsonTag {
			return true
		}
	}
	return false
}

// Bytes 将结构体序列化成byte数组
func (c *MysqlConf) Bytes() []byte {
	bytes, err := json.Marshal(c)
	if err != nil {
		return nil
	}
	return bytes
}

func parseKey(key string) string {
	if strings.Contains(key, "mysql.") {
		temps := strings.Split(key, ".")
		if len(temps) == 2 {
			return temps[1]
		} else {
			return key
		}
	} else {
		return key
	}
}
