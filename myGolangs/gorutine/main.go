package main

import (
	"fmt"
	"runtime"
)

func main() {
	gomaxprocs := runtime.GOMAXPROCS(runtime.NumCPU() + 16)
	fmt.Println(runtime.NumCPU(), "-gomax-", gomaxprocs)
	gomaxprocs2 := runtime.GOMAXPROCS(runtime.NumCPU() + 16)
	fmt.Println(runtime.NumCPU(), "-gomax-", gomaxprocs2)
	ch := make(chan int)
	go routineA(ch)
	go routineB(ch)
	println("goroutines scheduled!")
	<-ch
	<-ch
}
func routineA(ch chan int) {
	println("A executing!")
	ch <- 1
}
func routineB(ch chan int) {
	println("B executing!")
	ch <- 2
}
