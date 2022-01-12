package factory

import "sync"

type BeanFactory interface {

	// 注册工厂的名字
	Name() string
}

type factoryBean struct {

	// 防止协程竞争
	lock sync.RWMutex

	// 具体工厂内容
	factory map[string]interface{}
}

var content *factoryBean

func init() {
	content = &factoryBean{
		lock:    sync.RWMutex{},
		factory: make(map[string]interface{}),
	}
}

// 读写分离加锁
// 使用 BeanFactory 保证注册来的 factory 都是实现 BeanFactory 接口，
// 这样可以保证是通过实现的name来读取，显示的暴露使用方法
func Set(bean BeanFactory) {

	content.lock.Lock()
	defer content.lock.Unlock()

	content.factory[bean.Name()] = bean
}

// 读写分离加锁
func Get(bean BeanFactory) interface{} {

	content.lock.RLock()
	defer content.lock.RUnlock()

	return content.factory[bean.Name()]
}
