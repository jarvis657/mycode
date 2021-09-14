package main

import "fmt"

func main__() {
	data := "A\xfe\x02\xff\x04"
	for _, v := range data {
		fmt.Printf("%#x ", v)    // 0x41 0xfffd 0x2 0xfffd 0x4    // 错误
	}
	for _, v := range []byte(data) {
		fmt.Printf("%#x ", v)    // 0x41 0xfe 0x2 0xff 0x4    // 正确
	}
	x := 2
	y := 4
	table_ := make([][]int, x)
	for i := range table_ {
		table_[i] = make([]int, y)
	}
	h, w := 2, 4
	raw := make([]int, h*w)
	for i := range raw {
		raw[i] = i
	}
	// 初始化原始 slice
	fmt.Println("one:", raw, &raw[4]) // [0 1 2 3 4 5 6 7] 0xc420012120
	table := make([][]int, h)
	for i := range table {
		// 等间距切割原始 slice，创建动态多维数组 table
		// 0: raw[0*4: 0*4 + 4]
		// 1: raw[1*4: 1*4 + 4]
		table[i] = raw[i*w : i*w+w]
	}
	fmt.Println(table, &table[1][0]) // [[0 1 2 3] [4 5 6 7]] 0xc420012120
}
