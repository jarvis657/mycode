package main

import "fmt"

var cc = make(chan int, 4)

func main() {
	cc <- 1
	cc <- 2
	cc <- 3
	cc <- 4
	for x, _ := unBlockRead(cc); x > 0; x, _ = unBlockRead(cc) {
		fmt.Printf("%v\n", x)
	}
}

func unBlockRead(ch chan int) (x int, err error) {
	select {
	case x = <-ch:
		return x, nil
	default:
	}
	return 0, nil
}
