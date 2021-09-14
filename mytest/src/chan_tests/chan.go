package main

import (
	"context"
	"fmt"
	"time"
)

func main() {
	c := make(chan bool, 4)
	go watch(c, "【监控1】")
	go watch(c, "【监控2】")
	go watch(c, "【监控3】")

	time.Sleep(10 * time.Second)
	fmt.Println("可以了，通知监控停止")
	//chan 里面的只能被消费一次
	d := c
	//d <- true
	close(d)
	//c <- true
	//c <- true //因为上面多开了一个空间，所以能缓存，所以不会又dead lock
	//close(c)
	//为了检测监控过是否停止，如果没有监控输出，就表示停止了
	time.Sleep(5 * time.Second)
	fmt.Println("退出拉。。。。。")
}

func watch(ctx <-chan bool, name string) {
	for {
		select {
		case <-ctx:
			fmt.Println(name, "监控退出，停止了...")
			return
		default:
			fmt.Println(name, "goroutine监控中...")
			time.Sleep(2 * time.Second)
		}
	}
}

func ctx_main() {
	ctx, cancel := context.WithCancel(context.Background())
	go func(cx context.Context) {
		ctx2, cancel2 := context.WithCancel(cx)
		for {
			select {
			case <-ctx2.Done():
				fmt.Println("sub监控退出，停止了...")
				return
			default:
				fmt.Println("sub goroutine监控中...")
				time.Sleep(2 * time.Second)
				cancel2()
			}
		}
	}(ctx)
	time.Sleep(10 * time.Second)
	go ctx_watch(ctx, "【监控1】")
	go ctx_watch(ctx, "【监控2】")
	go ctx_watch(ctx, "【监控3】")

	time.Sleep(10 * time.Second)
	fmt.Println("可以了，通知监控停止")
	cancel()
	//为了检测监控过是否停止，如果没有监控输出，就表示停止了
	time.Sleep(5 * time.Second)
}

func ctx_watch(ctx context.Context, name string) {
	for {
		select {
		case <-ctx.Done():
			fmt.Println(name, "监控退出，停止了...")
			return
		default:
			fmt.Println(name, "goroutine监控中...")
			time.Sleep(2 * time.Second)
		}
	}
}
