package main

import "fmt"

func main() {
	var a = [5]int{1, 2, 3, 4, 5}
	var r [5]int

	fmt.Println("original a =", a)

	for i, v := range a {
		fmt.Printf("inner i:%v,v:%v\n", i, v)
		if i == 0 {
			a[1] = 11
			a[2] = 12
			a[3] = 13
			a[4] = 14
			fmt.Println("inner arr, a =", a)
		}
		r[i] = v
	}

	fmt.Println("after for range loop, r =", r)
	fmt.Println("after for range loop, a =", a)
}
