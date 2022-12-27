package main

import (
	"context"
	"fmt"
	"golang.org/x/sync/errgroup"
	"testing"
)

type IceCreamMaker interface {
	Hello()
}

type Ben struct {
	//panic: runtime error: invalid memory address or nil pointer dereference
	//[signal SIGSEGV: segmentation violation code=0x1 addr=0x5 pc=0x105ed77]
	//
	//goroutine 1 [running]:
	//fmt.(*buffer).writeString(...)
	//        /usr/local/opt/go/libexec/src/fmt/print.go:82
	//fmt.(*fmt).padString(0xc0000061a0?, {0x5, 0x10d3b00})
	//        /usr/local/opt/go/libexec/src/fmt/format.go:110 +0x247
	//fmt.(*fmt).fmtS(0x105a6d5?, {0x5?, 0x12?})
	//        /usr/local/opt/go/libexec/src/fmt/format.go:359 +0x3f
	//fmt.(*pp).fmtString(0x107b380?, {0x5?, 0xc00013c000?}, 0x14?)
	//        /usr/local/opt/go/libexec/src/fmt/print.go:477 +0xc5
	//fmt.(*pp).printArg(0xc0001188f0, {0x109eca0?, 0xc000014c80}, 0x73)
	//        /usr/local/opt/go/libexec/src/fmt/print.go:725 +0x21e
	//fmt.(*pp).doPrintf(0xc0001188f0, {0x10b42e6, 0x13}, {0xc000108f18?, 0x1, 0x1})
	//可能出现上面的问题。所以id 不能有
	id   int
	name string
}

func (b *Ben) Hello() {
	fmt.Printf("ben .. name is %s \n", b.name)
}

type Jerry struct {
	name string
}

func (j *Jerry) Hello() {
	fmt.Printf("jerry .. name is %s \n", j.name)
}

func main() {
	errgroup.WithContext(context.Background())
	//fmt.Println("aaaaa")
	//select {}
	//fmt.Println("zzzzzzzzzzzzz")
	//var ben = &Ben{"Ben"}
	var ben = &Ben{10, "Ben"}
	var jerry = &Jerry{"jerry"}
	var maker IceCreamMaker = ben
	var loop0, loop1 func()
	loop0 = func() {
		maker = ben
		go loop1()
	}
	loop1 = func() {
		maker = jerry
		go loop0()
	}
	go loop0()
	for {
		maker.Hello()
	}
}

func TestCheckingChannel(t *testing.T) {
	stop := make(chan bool)

	// Testing some fucntion that SHOULD close the channel
	func(stop chan bool) {
		close(stop)
	}(stop)

	// Make sure that the function does close the channel
	_, ok := <-stop

	// If we can recieve on the channel then it is NOT closed
	if ok {
		t.Error("Channel is not closed")
	}
}
