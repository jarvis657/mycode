package main

import (
	"bytes"
	"fmt"
	"strings"
	"sync"
	"time"
)

type myMutex sync.Mutex

func get() []byte {
	//var  mm  myMutex
	raw := make([]byte, 10000)
	fmt.Println(len(raw), cap(raw), &raw[0]) // 10000 10000 0xc420080000
	return raw[:3]                           // 重新分配容量为 10000 的 slice
}

//func get() (res []byte) {
//	raw := make([]byte, 10000)
//	fmt.Println(len(raw), cap(raw), &raw[0])    // 10000 10000 0xc420080000
//	res = make([]byte, 3)
//	copy(res, raw[:3])
//	return
//}
type field struct {
	name string
}

func (p *field) print() {
	fmt.Println(p.name)
}

type data struct {
	name string
}

type printer interface {
	print()
}

func (p *data) print() {
	fmt.Println("name: ", p.name)
}

func testm1(m map[int32]string) map[int32]string {
	delete(m, 1)
	return m
}
func testm2(m map[int32]string) map[int32]string {
	delete(m, 2)
	return m
}

type myType int32
type myType2 int

func main_aa() {
	type2 := myType2(1)
	type22 := myType(1)
	fmt.Printf("%v,%v,%v,%v", type2, type22, type22 == 1, type2 == 1)

	mm := make(map[int32]string)
	mm[1] = "1"
	mm[2] = "2"
	mm[3] = "3"
	m2 := testm1(mm)
	fmt.Printf("m:%v", m2)
	m3 := testm2(mm)
	fmt.Printf("m:%v", m3)

	fmt.Printf("m:%v", m2)
	ids := make([]int32, 5)
	ids[0] = int32(1)
	ids[1] = int32(2)
	ids[2] = int32(3)
	ids[3] = int32(4)
	ids[4] = int32(5)
	fmt.Printf("data:%s\n", ids)
	fmt.Printf("data:%s\n", []byte("Go语言"))

	tsid := fmt.Sprintf("%s%s%s", "a", "\001", "b")
	split := strings.Split(tsid, "\001")
	fmt.Println(split[0])
	fmt.Println(split[1])

	d1 := data{"one"}
	d1.print() // d1 变量可寻址，可直接调用指针 receiver 的方法

	var in printer = &data{"two"} //只能改成指针，可寻址就行， 如果是data类型 就会报错 --原因是 使用 func (p *data) print() 这个方式，把*去掉就可以
	in.print()                    // 类型不匹配

	m := map[string]*data{
		"x": &data{"three"},
	}
	m["x"].print() // m["x"] 是不可寻址的  -- 需要将对应的value改成指针   // 变动频繁

	datas := []field{{"one"}, {"two"}, {"three"}}
	for _, vvv := range datas {
		go vvv.print()
	}
	//trace.Start(os.Stderr)
	//defer trace.Stop()
	//for i := 0; i <10000 ; i++ {
	//	data := get()
	//	fmt.Println(len(data), cap(data), &data[0]) // 3 10000 0xc420080000
	//}
	//ch := make(chan string)
	//go func() {
	//	ch <- "EDDYCJY"
	//}()
	//<-ch
	time.Sleep(time.Second * 5)
	path := []byte("AAAA/BBBBBBBBB")
	sepIndex := bytes.IndexByte(path, '/') // 4
	println(sepIndex)

	//dir1 := path[:sepIndex]   // 如果这么写 最后的 就会是错误结果
	dir1 := path[:sepIndex:sepIndex] //第三个参数是用来控制 dir1 的新容量，再往 dir1 中 append 超额元素时，将分配新的 buffer 来保存。而不是覆盖原来的 path 底层数组
	dir2 := path[sepIndex+1:]
	println("dir1: ", string(dir1)) // AAAA
	println("dir2: ", string(dir2)) // BBBBBBBBB

	dir1 = append(dir1, "suffix"...)
	println("current path: ", string(path)) // AAAAsuffixBBBB

	path = bytes.Join([][]byte{dir1, dir2}, []byte{'/'})
	println("dir1: ", string(dir1)) // AAAAsuffix
	println("dir2: ", string(dir2)) // uffixBBBB

	println("new path: ", string(path)) // AAAAsuffix/uffixBBBB    // 错误结果
}
