package main

import (
	"fmt"
	"sync"
	"sync/atomic"
	"time"
)

type Origin struct {
	a uint64
	b uint64
}

//WithPadding getconf -a | grep CACHE
type WithPadding struct {
	a uint64
	_ [56]byte
	b uint64
}

var num = 10000 * 1000

func OriginParallel() {
	var v Origin

	var wg sync.WaitGroup
	wg.Add(2)

	go func() {
		for i := 0; i < num; i++ {
			atomic.AddUint64(&v.a, 1)
		}
		wg.Done()
	}()

	go func() {
		for i := 0; i < num; i++ {
			atomic.AddUint64(&v.b, 1)
		}
		wg.Done()
	}()

	wg.Wait()
	fmt.Printf("origin:%v \n", v.a+v.b)

}

func WithPaddingParallel() {
	var v WithPadding

	var wg sync.WaitGroup
	wg.Add(2)

	go func() {
		for i := 0; i < num; i++ {
			atomic.AddUint64(&v.a, 1)
		}
		wg.Done()
	}()

	go func() {
		for i := 0; i < num; i++ {
			atomic.AddUint64(&v.b, 1)
		}
		wg.Done()
	}()

	wg.Wait()
	fmt.Printf("padding:%v \n", v.a+v.b)
}

//内存对齐 访问速度5倍+
func main() {
	ss := make([][]int, 5)
	fmt.Printf("%v,%v", len(ss), len(ss))
	var b time.Time

	b = time.Now()
	OriginParallel()
	fmt.Printf("OriginParallel. Cost=%+v.\n", time.Now().Sub(b))

	b = time.Now()
	WithPaddingParallel()
	fmt.Printf("WithPaddingParallel. Cost=%+v.\n", time.Now().Sub(b))
}

//func main() {
//	c := sync.NewCond(&sync.Mutex{})
//	for i := 0; i < 10; i++ {
//		go listen(c)
//	}
//	time.Sleep(1 * time.Second)
//	go broadcast(c)
//
//	ch := make(chan os.Signal, 1)
//	signal.Notify(ch, os.Interrupt)
//	<-ch
//}
//
//func broadcast(c *sync.Cond) {
//	c.L.Lock()
//	atomic.StoreInt64(&status, 1)
//	c.Broadcast()
//	c.L.Unlock()
//}
//
//func listen(c *sync.Cond) {
//	c.L.Lock()
//	for atomic.LoadInt64(&status) != 1 {
//		c.Wait()
//	}
//	fmt.Println("listen")
//	c.L.Unlock()
//}
