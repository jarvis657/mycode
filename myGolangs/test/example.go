package main

import (
	"fmt"
	"unsafe"
)

type emtpyInterface struct {
	typ  *struct{}
	word unsafe.Pointer
}

func swap(a, b interface{}) {
	*(*int)((*emtpyInterface)(unsafe.Pointer(&a)).word) = 2
	*(*int)((*emtpyInterface)(unsafe.Pointer(&b)).word) = 1
	fmt.Println(a, b)
}

//打印
//2 1
//2 1
//但是注释第29行后打印
//1 2
//
//因为使用空接口赋值参数，他的实际内存结构是emptyInterface，如果赋值的是指针emptyInterface.word字段存的就是这个指针本身，如果赋值的一个值，那么存的是这个值的指针，于是想验证一下老生常谈的使用地址swap和使用值swap的问题。根据上面的理解，将a,b的值传入swap函数之后，承接的interface{}类型实际是a,b的指针，于是使用寻址的方式来修改a,b的值，那么main方法中a,b的值也会被修改。第一次打印结果印证了我的想法。但是当我注视第29行之后再打印，a,b的值却没有改变，有大佬知道这是为什么吗
func main() {
	a, b := 10, 20
	swap(a, b)
	fmt.Println(a, b)
}
