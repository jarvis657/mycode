package main

import (
	"fmt"
	"github.com/gammazero/workerpool"
	"github.com/robfig/cron"

	"time"
)

func main() {
	c := cron.New()
	c.AddFunc("*/1 * * * * *", func() {
		wp := workerpool.New(4)
		requests := []string{"alpha", "beta", "gamma", "delta", "epsilon", "vv", "xx"}
		for _, r := range requests {
			r := r
			wp.Submit(func() {
				if r == "beta" {
					time.Sleep(20 * time.Second)
				}
				fmt.Println("Handling request:", r)
			})
		}
		wp.StopWait()
	})
	c.Start()
	time.Sleep(100 * time.Second)
}
