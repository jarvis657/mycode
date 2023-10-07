package main

import (
	"context"
	"fmt"
	"time"
)

const shortDuration = 3 * time.Second

func main() {
	tooSlow := fmt.Errorf("too slow!")
	ctx, cancel := context.WithTimeoutCause(context.Background(), shortDuration, tooSlow)
	defer cancel()
	context.AfterFunc(ctx, func() {
		fmt.Println("invoked..")
		cancel()
	})
	st := context.AfterFunc(ctx, func() {
		fmt.Println("invoked..222")
		//cancel()
	})
	//调用st 含义是将 这个函数(invoked..222)和ctx解绑
	b := st()
	fmt.Printf("sss st:%v\n", b)

	//time.Sleep(1 * time.Second)
	//fmt.Println(ctx.Err())
	//fmt.Println(context.Cause(ctx))
	select {
	case <-time.After(50 * time.Second):
		fmt.Println("overslept")
	case <-ctx.Done():
		fmt.Println(ctx.Err())
		fmt.Println(context.Cause(ctx))
	}
	time.Sleep(2 * time.Second)
}
