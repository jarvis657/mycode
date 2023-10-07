package main

import (
	"fmt"
	"math/rand"
	"reflect"
	"runtime"
	"sync"
	"time"

	"github.com/go-co-op/gocron"
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

// 获取正在运行的函数名
func runFuncName() string {
	pc := make([]uintptr, 1)
	runtime.Callers(2, pc)
	f := runtime.FuncForPC(pc[0])
	return f.Name()
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
	m := make(map[Foo]string)
	bar := &Bar{"one"}
	foo := Foo{bar}
	m[foo] = "foo"
	fmt.Printf("foo: %s bar one:%s\n", foo, m[foo])
	bar.Id = "two"
	fmt.Printf("bar id change two:%s\n", m)

	bar2 := &Bar{"two"}
	foo2 := Foo{bar2}

	fmt.Printf("foo2:%s  foo:%s  foo2==foo:%s\n", foo2, foo, foo2 == foo)
	fmt.Printf("reflect.DeepEqual(a, b): %s \n", reflect.DeepEqual(foo2, foo))

	fmt.Printf("foo2:%s  :%s\n", foo2, m[foo2])
	// At this point, your map may be irreversibly broken.
	// It contains an element that is probably in the wrong bucket.
	fmt.Println("================================================")
	query := map[string]string{}
	// 需要按照字典排序
	query["test1"] = "1"
	query["test0"] = "0"
	query["test2"] = "2"

	//for i := 0; i < 100; i++ {
	for i, v := range query {
		fmt.Println(i, " # ", v)
	}
	//}
	ch := make(chan bool, 2)
	ch <- true
	ch <- true
	close(ch)
	fmt.Println("len:==========cap:{}", len(ch), cap(ch))
	for i := 0; i < cap(ch)+2; i++ {
		v, ok := <-ch
		fmt.Println(i, v, ok)
	}
	fmt.Println("testSelect======================================")
	testSelect()
}
func testSelect() {
	fmt.Println(runFuncName(), " 测试函数调用:")
	finish := make(chan bool)
	var done sync.WaitGroup
	done.Add(1)
	go func() {
		select {
		case <-time.After(2 * time.Second):
			fmt.Println("v")
		case <-finish:
		}
		fmt.Println("done before")
		done.Done()
	}()
	t0 := time.Now()
	//<-time.After(3 * time.Second) //如果这里的时间超过 select case上的2秒 则 fatal error: all goroutines are asleep - deadlock!出现这个错误,因为finish是无buffer的channel,无buffer是'先消费后生产',
	//// 不然会堵塞生产者 将channel改成有buffer可解决这个问题
	//finish <- true // send the close signal
	close(finish)
	fmt.Println("done wait")
	done.Wait() // wait for the goroutine to stop
	fmt.Printf("Waited %v for goroutine to stop\n", time.Since(t0))
}
