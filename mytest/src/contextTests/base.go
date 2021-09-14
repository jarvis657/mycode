package main

import (
	"context"
	"fmt"
	"time"
)

func main() {
	ctx, cancel := context.WithTimeout(context.Background(), 1*time.Second)
	defer cancel()

	go handle(ctx, 2*time.Second)
	select {
	case <-ctx.Done():
		fmt.Println("main ..... ", ctx.Err())
	}
	time.Sleep(100 * time.Millisecond)
}

func handle(ctx context.Context, duration time.Duration) {
	select {
	case <-ctx.Done():
		fmt.Println("handle .......... ", ctx.Err())
	case <-time.After(duration):
		fmt.Println("process request with", duration)
	}
}
