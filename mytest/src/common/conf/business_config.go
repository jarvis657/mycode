package conf

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"mytest.com/src/common/constant"
	"git.code.oa.com/trpc-go/trpc-go/config"
	"github.com/spf13/cast"
)

var (
	BusinessKeys = []string{
		constant.RainbowStkePubKey,
		constant.RainbowStkePrivKey,
		constant.ZhiyunPWDKey,
	}
)

type BusinessConf struct {
	ConfigMap map[string]interface{}
}

// Load 从七彩石加载配置
func (c *BusinessConf) Load() error {
	ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
	defer cancel()
	conf, err := config.Get(constant.RainbowProviderName).Get(ctx, constant.RainbowBusinessKey)
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
func (c *BusinessConf) Reload() {
	c.Load()
}

// GetString 根据key获取对应字段的string值，找不到key返回默认值
func (c *BusinessConf) GetString(key string, defaultValue string) string {
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

// Unmarshal 将BusinessConf反序列化为对应输入的结构体
func (c *BusinessConf) Unmarshal(in interface{}) error {
	return json.Unmarshal(c.Bytes(), in)
}

// Get 根据key获取对应字段的值，找不到key返回默认值
func (c *BusinessConf) Get(key string, defaultValue interface{}) interface{} {
	if v, err := c.find(key); err == nil {
		return v
	}

	return defaultValue
}

// GetInt 根据key读取int类型配置，找不到key返回默认值
func (c *BusinessConf) GetInt(key string, defaultValue int) int {
	return cast.ToInt(c.findWithDefaultValue(key, defaultValue))
}

// GetInt32 GetInt
func (c *BusinessConf) GetInt32(key string, defaultValue int32) int32 {
	return cast.ToInt32(c.findWithDefaultValue(key, defaultValue))
}

// GetInt64 根据key读取int64类型配置
func (c *BusinessConf) GetInt64(key string, defaultValue int64) int64 {
	return cast.ToInt64(c.findWithDefaultValue(key, defaultValue))
}

// GetUint 根据key读取int类型配置
func (c *BusinessConf) GetUint(key string, defaultValue uint) uint {
	return cast.ToUint(c.findWithDefaultValue(key, defaultValue))
}

// GetUint32 根据key读取uint32类型配置
func (c *BusinessConf) GetUint32(key string, defaultValue uint32) uint32 {
	return cast.ToUint32(c.findWithDefaultValue(key, defaultValue))
}

// GetUint64 根据key读取uint64类型配置
func (c *BusinessConf) GetUint64(key string, defaultValue uint64) uint64 {
	return cast.ToUint64(c.findWithDefaultValue(key, defaultValue))
}

// GetFloat64 根据key读取float64类型配置
func (c *BusinessConf) GetFloat64(key string, defaultValue float64) float64 {
	return cast.ToFloat64(c.findWithDefaultValue(key, defaultValue))
}

// GetFloat32 根据key读取float32类型配置
func (c *BusinessConf) GetFloat32(key string, defaultValue float32) float32 {
	return cast.ToFloat32(c.findWithDefaultValue(key, defaultValue))
}

// GetBool 根据key读取bool类型配置
func (c *BusinessConf) GetBool(key string, defaultValue bool) bool {
	return cast.ToBool(c.findWithDefaultValue(key, defaultValue))
}

func (c *BusinessConf) findWithDefaultValue(key string, defaultValue interface{}) interface{} {
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

func (c *BusinessConf) find(key string) (interface{}, error) {
	if val, ok := c.ConfigMap[key]; ok {
		return val, nil
	} else {
		return nil, fmt.Errorf("key not find")
	}
}

// IsSet 根据key判断是否在map中
func (c *BusinessConf) IsSet(key string) bool {
	if _, ok := c.ConfigMap[key]; ok {
		return true
	} else {
		return false
	}
}

// Bytes 将结构体序列化成byte数组
func (c *BusinessConf) Bytes() []byte {
	bytes, err := json.Marshal(c.ConfigMap)
	if err != nil {
		return nil
	}
	return bytes
}
