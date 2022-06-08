package main

import (
	"github.com/robfig/cron/v3"
)

func main_() {
	c := cron.New()
	c.AddFunc(
		"*/2 * * * * *", func() {
		},
	)
	c.Start()
	for true {

	}
}
