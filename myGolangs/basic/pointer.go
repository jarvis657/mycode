package main

import (
	"fmt"
	"unsafe"
)

type Person struct {
	id     int64   // 对齐系数 8，占用 8 字节
	height float32 // 对齐系数 4，占用 4 字节
	age    int16   // 对齐系数 2，占用 2 字节
	age2   int16   // 对齐系数 2，占用 2 字节
	age3   int8    // 对齐系数 1，占用 1 字节
	//empty  struct{}
}

func main() {
	var t int8
	fmt.Printf("t地址：%p 占用 %d 字节，对齐系数：%d\n", &t, unsafe.Sizeof(t), unsafe.Alignof(t))
	var a Person
	fmt.Println("=========================")
	// 地址：0xc00000e370 占用 1 字节，对齐系数：1
	fmt.Printf("地址：%p 占用 %d 字节，对齐系数：%d\n", &a, unsafe.Sizeof(a), unsafe.Alignof(a))
	fmt.Println("================================")
}
