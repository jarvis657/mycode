package main

import (
	"fmt"
	"github.com/go-co-op/gocron"
	"math/rand"
	"sync"
	"time"
)

type Bar struct {
	Id string
}

type Foo struct {
	Bar *Bar
}

func (f *Foo) Equal(f2 *Foo) bool {
	return f.Bar.Id == f2.Bar.Id
}

func (f *Foo) Hash() uint32 {
	var h uint32
	for i := 0; i < len(f.Bar.Id); i++ {
		h ^= uint32(f.Bar.Id[i])
		h *= 16777619
	}
	return h
}

var expireRenderDetails = sync.Map{}

func printMap(m *sync.Map) {
	fmt.Printf("%d==============================\n", time.Now().Unix())
	m.Range(func(k, v interface{}) bool {
		fmt.Printf("printMap:%v,%v,\n", k, v)
		return true
	})
}

func put(m *sync.Map, key, value interface{}) {
	fmt.Printf("put:%v,%v,\n", key, value)
	m.Store(key, value)
}

func randPut() {
	min := 10
	max := 30
	i := rand.Intn(max-min) + min
	var value = time.Now().Unix() + int64(i)
	put(&expireRenderDetails, i, value)
}

type SupplierChatGPTConfig struct {
	UserTokensPerDayLimit int `json:"user_tokens_per_day_limit"`
	TokenConfig           []struct {
		Account   string `json:"account"`
		Token     string `json:"token"`
		IsDisable bool   `json:"is_disable"`
	} `json:"tokenConfig"`
}

func main() {
	s := gocron.NewScheduler(time.UTC)
	s.Every(10).Seconds().Do(func() {
		fmt.Printf("%v %v\n", time.Now().Format("20060102150405"), time.Now().Unix())
	})
	s.StartAsync()
	for {
		time.Sleep(1 * time.Second)
	}
	//date := time.Now().AddDate(0, 0, 2)
	//fmt.Println(date.Format("20060102"))
	//var config = SupplierChatGPTConfig{}
	//supplierConfig := `{"user_tokens_per_day_limit":10000,"tokenConfig":[{"account":"default","token":"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","is_disable":false}]}`
	//json.Unmarshal([]byte(supplierConfig), &config)
	//fmt.Println(config.TokenConfig)
	//now := time.Now()
	//fmt.Println(now.Format("20060102"))
	//randPut()
	//randPut()
	//randPut()
	//randPut()
	//randPut()
	//printMap(&expireRenderDetails)
	//expireRenderDetails.Range(func(k, v interface{}) bool {
	//	if 15 < k.(int) {
	//		fmt.Printf("del:%v\n", k.(int))
	//		expireRenderDetails.Delete(k)
	//	}
	//	//printMap(&expireRenderDetails)
	//	return true
	//})
	//printMap(&expireRenderDetails)
	//
	//time.Sleep(time.Duration(5) * time.Second)
	//expireRenderDetails.Store("f", "f")
	//printMap(&expireRenderDetails)
	//
	//m := make(map[Foo]string)
	//bar := &Bar{"one"}
	//foo := Foo{bar}
	//m[foo] = "foo"
	//fmt.Printf("foo: %s bar one:%s\n", foo, m[foo])
	//fmt.Printf("foo: %s bar one:%s\n", foo, m[foo])
	//bar.Id = "two"
	//fmt.Printf("bar id change two:%s\n", m)
	//
	//bar2 := &Bar{"two"}
	//foo2 := Foo{bar2}
	//
	//fmt.Printf("foo2:%s  foo:%s  foo2==foo:%s\n", foo2, foo, foo2 == foo)
	//fmt.Printf("reflect.DeepEqual(a, b): %s \n", reflect.DeepEqual(foo2, foo))
	//
	//fmt.Printf("foo2:%s  :%s\n", foo2, m[foo2])
	//// At this point, your map may be irreversibly broken.
	//// It contains an element that is probably in the wrong bucket.
	//fmt.Println("================================================")
	//query := map[string]string{}
	//// 需要按照字典排序
	//query["test0"] = "0"
	//query["test1"] = "1"
	//query["test2"] = "2"
	//
	//for i := 0; i < 100; i++ {
	//	for _, v := range query {
	//		fmt.Print(v)
	//	}
	//	fmt.Println()
	//}
}
