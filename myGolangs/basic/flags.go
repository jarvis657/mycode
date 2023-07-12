package main

import (
	"flag"
	"fmt"
	"strconv"
	"strings"
)

// 定义一个类型为[]string的自定义flag
type arrayFlags []string

// 实现flag.Value接口
func (i *arrayFlags) String() string {
	return "my string representation"
}

func (i *arrayFlags) Set(value string) error {
	*i = strings.Split(value, ",")
	return nil
}

var myFlags arrayFlags

func main() {
	vs := []string{"1", "2", "3", "4", "5", "6", "7", "8", "9"}
	fmt.Println(vs[1:3])
	var currency int
	var size int
	var model string
	var ids arrayFlags
	flag.IntVar(&currency, "currency", 12, "The currency")
	flag.IntVar(&size, "size", 100, "size")
	flag.StringVar(&model, "model", "", "model")
	flag.Var(&myFlags, "list", "Input a list of comma-separated strings.")
	flag.Parse()
	ids_ := make([]int, 0)
	for _, i := range ids {
		atoi, _ := strconv.Atoi(i)
		ids_ = append(ids_, atoi)
	}
	fmt.Println("currency", currency)
	fmt.Println("size", size)
	fmt.Println("model", model)
	fmt.Println("ids......................")
	for i, item := range ids_ {
		fmt.Println(i, item)
	}
}
