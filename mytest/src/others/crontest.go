package main

import (
	"git.code.oa.com/trpc-go/trpc-go/log"
	"github.com/robfig/cron/v3"
	"time"
)

func main_() {
	c := cron.New()
	c.AddFunc("*/2 * * * * *", func() {
		log.Infof("running deploy............time:%v", time.Now().Unix()/1000)
	})
	c.Start()
	for true {

	}
}
