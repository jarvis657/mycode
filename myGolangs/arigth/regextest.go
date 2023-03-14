package main

import (
	"fmt"
	"regexp"
	"sync/atomic"
	"time"
)

var index = new(int64)

func main() {

	s := time.Now().Format("2006-01-02 15:04:05")
	fmt.Printf("%v\n", s)

	atomic.AddInt64(index, 1)
	fmt.Println(atomic.LoadInt64(index))
	fmt.Println(atomic.LoadInt64(index))

	now := time.Now()
	format := now.Format("Jan 02 2006")
	timeFormat := now.Format("15:04")
	fmt.Printf("Format:%v\n", format)
	fmt.Printf("Format:%v\n", timeFormat)

	ss := `I want you to act as an app naming helper. You will help me come up with a unique and catchy name for a {{Industries}} app that is focused on {{Crowd}}. The name should be {{Features}}, and should reflect the app's purpose. Give me at least five different name suggestions.`
	//r := regexp.MustCompile(`(?P<Year>\d{4})-(?P<Month>\d{2})-(?P<Day>\d{2})`)
	r := regexp.MustCompile(`\{\{(\w*)\}\}*`)
	fmt.Printf("%#v\n", r.FindStringSubmatch(ss))
	submatch := r.FindAllStringSubmatch(ss, -1)
	for i, strings := range submatch {
		fmt.Printf("%d: %#v\n", i, strings)
	}
	fmt.Printf("%#v\n", submatch)
	fmt.Printf("%#v\n", r.SubexpNames())

}
