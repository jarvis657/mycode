package main

import (
	"fmt"
	"sync/atomic"
	"unsafe"
)

type noCopy struct{}
type align64 struct{}

type Uint64 struct {
	_     noCopy
	state atomic.Uint64
	v2    uint32
}

type A struct {
	a int8
	b int32
	c int16
}

type B struct {
	a int8
	c int16
	b int32
}

type C struct {
	m struct{} // 0
	n int8     // 1
}

var c C

func main() {
	fmt.Printf("%d\n", 0x85ebca6b)
	fmt.Printf("arrange fields to reduce size:\n"+"A align: %d, size: %d\n", unsafe.Alignof(A{}), unsafe.Sizeof(A{}))
	fmt.Printf("arrange fields to reduce size:\n"+"B align: %d, size: %d\n", unsafe.Alignof(B{}), unsafe.Sizeof(B{}))
	fmt.Println(unsafe.Sizeof(c))
}
