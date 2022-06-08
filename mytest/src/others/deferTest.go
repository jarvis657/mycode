package main

import "fmt"

type holder struct {
	val int
}

// 通过这个可以看出参数传递也是值传递
func deferFunc(h *holder) *holder {
	h.val = -1111
	fmt.Printf("defer func called %p,%v\n", h, h)
	h = &holder{} //打印出来的跟调用的不一样
	h.val = 111
	fmt.Printf("defer func called %p,%v\n", h, h)
	return h
}

func returnFunc(h *holder) *holder {
	fmt.Println("return func called")
	h.val = 1
	return h
}

func returnAndDefer(h *holder) *holder {

	defer deferFunc(h)

	i := returnFunc(h)
	return i
}

func main() {
	h := &holder{val: 0}
	fmt.Printf("%p,%v\n", h, h)
	andDefer := returnAndDefer(h)
	fmt.Printf("%p,%v\n", andDefer, andDefer)
}
