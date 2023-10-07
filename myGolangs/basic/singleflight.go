package main

import (
	"fmt"
	"time"

	"golang.org/x/sync/singleflight"
)

func getData(id int64) string {
	fmt.Printf("inner query...%d\n", id)
	time.Sleep(5 * time.Second) // 模拟一个比较耗时的操作
	return fmt.Sprintf("liwenzhou.com %d", id)
}

func main() {
	g := new(singleflight.Group)
	// 第1次调用
	go func() {
		v1, _, shared := g.Do("getData", func() (interface{}, error) {
			ret := getData(1)
			return ret, nil
		})
		fmt.Printf("1st call: v1:%v, shared:%v\n", v1, shared)
	}()
	time.Sleep(2 * time.Second)
	// 第2次调用（第1次调用已开始但未结束）
	go func() {
		v2, _, shared := g.Do("getData", func() (interface{}, error) {
			ret := getData(1)
			return ret, nil
		})
		fmt.Printf("2nd call: v2:%v, shared:%v\n", v2, shared)
	}()
	// 第3次调用（第1次调用已开始但未结束）
	v3, _, shared := g.Do("getData", func() (interface{}, error) {
		ret := getData(1)
		return ret, nil
	})
	fmt.Printf("3nd call: v3:%v, shared:%v\n", v3, shared)
	time.Sleep(1 * time.Minute)
}
