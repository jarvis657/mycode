package main

import (
	"fmt"
	"io"
	"os"
	"reflect"
	"time"
	"unsafe"
)

type Foo struct {
	FirstName string `tag_name:"tag 1",modify2DB:true`
	LastName  string `tag_name:"tag 2",tag2_name:"tag2"`
	Age       int    `tag_name:"tag 3"`
}

func inspect(f interface{}) map[string]interface{} {
	m := make(map[string]interface{})
	val := reflect.ValueOf(f).Elem()
	for i := 0; i < val.NumField(); i++ {
		valueField := val.Field(i)
		typeField := val.Type().Field(i)
		f := valueField.Interface()
		val := reflect.ValueOf(f)
		index := typeField.Index
		fmt.Printf("tag:%v,index:%+v\n", typeField.Tag.Get("tag_name"), index)
		m[typeField.Tag.Get("tag_name")] = val.Interface()
	}
	return m
}

func dump(m map[string]interface{}) {
	for k, v := range m {
		fmt.Printf("%v : %v\n", k, v)
	}
}

func isChanClose(ch chan int) bool {
	select {
	case _, received := <-ch:
		return !received
	default:
	}
	return false
}

//判断chan是否关闭
func isChanClosed(ch interface{}) bool {
	if reflect.TypeOf(ch).Kind() != reflect.Chan {
		panic("only channels!")
	}
	cptr := *(*uintptr)(
		unsafe.Pointer(uintptr(unsafe.Pointer(&ch)) + unsafe.Sizeof(uint(0))),
	)
	// this function will return true if chan.closed > 0
	// see hchan on https://github.com/golang/go/blob/master/src/runtime/chan.go
	//type hchan struct {
	//	qcount   uint           // total data in the queue
	//	dataqsiz uint           // size of the circular queue
	//	buf      unsafe.Pointer // points to an array of dataqsiz elements
	//	elemsize uint16
	//	closed   uint32
	//	elemtype *_type // element type
	//	sendx    uint   // send index
	//	recvx    uint   // receive index
	//	recvq    waitq  // list of recv waiters
	//	sendq    waitq  // list of send waiters
	//
	//	// lock protects all fields in hchan, as well as several
	//	// fields in sudogs blocked on this channel.
	//	//
	//	// Do not change another G's status while holding this lock
	//	// (in particular, do not ready a G), as this can deadlock
	//	// with stack shrinking.
	//	lock mutex
	//}
	cptr += unsafe.Sizeof(uint(0)) * 2
	cptr += unsafe.Sizeof(uintptr(0))
	cptr += unsafe.Sizeof(uint16(0))
	return *(*uint32)(unsafe.Pointer(cptr)) > 0
}

//func main() {
//	f := &Foo{
//		FirstName: "Drew",
//		LastName:  "Olson",
//		Age:       30,
//	}
//	a := inspect(f)
//	dump(a)
//}
func concatFive(a, b, c, d, e string) string {
	return a + b + c + d + e
}
func concatTwo(a, b string) string {
	return a + b
}
func main() {
	two := concatTwo("a", "b")
	fmt.Println(two)
	five := concatFive("1", "2", "3", "4", "5")
	fmt.Println(five)
	//ch := make(chan int, 5)
	//fmt.Println(isChanClose(ch))
	//ch <- 5
	//ch <- 4
	//ch <- 3
	//ch <- 2
	//go func() {
	//	time.Sleep(1 * time.Second)
	//	close(ch)
	//}()
	//time.Sleep(2 * time.Second)
	////如果换成 isChanClose(ch) 还是会报错 因为 这个里面有缓存
	//if !isChanClosed(ch) {
	//	ch <- 1
	//}
	//虽然关闭 但是 有缓存也就没关闭
	//fmt.Println(isChanClose(ch))

	//closed := isChanClosed(ch)
	//fmt.Println(closed)
	//closed = isChanClosed(ch)
	//fmt.Println(closed)
	xs := []string{"a", "b", "c"}
	is := []int{1, 2, 3, 4, 5, 6, 7}
	js := []int{11, 22, 33, 44, 55, 66, 77}
	for _, x := range xs {
		fmt.Printf("for...%v ", x)
	RERUN:
		for _, i := range is {
			for _, j := range js {
				if j == 22 {
					//break
					break RERUN
				}
				fmt.Printf("x:%v,i:%v,j:%v\n", x, i, j)
			}
		}
		fmt.Printf("end for...%v\n", x)
	}
	// As interface types are only used for static typing, a
	// common idiom to find the reflection Type for an interface
	// type Foo is to use a *Foo value.
	var teststring string
	fmt.Println(len(teststring))
	fmt.Println(time.Now().Format(time.RFC3339))

	writerType := reflect.TypeOf((*io.Writer)(nil)).Elem()

	fileType := reflect.TypeOf((*os.File)(nil))
	fmt.Println(fileType.Implements(writerType))
	f := Foo{
		FirstName: "Drew",
		LastName:  "Olson",
		Age:       30,
	}
	Say(f)
	inspect(&f)
}

// Say should use struct field tags to postfix marked fields with `pretty please`.
func Say(v interface{}) {
	t := reflect.TypeOf(v)
	fmt.Printf("%+v\n", t)
	for i := 0; i < t.NumField(); i++ {
		fmt.Printf("%+v\n", t.Field(i))
		fmt.Printf("%v\n", t.Field(i).Tag)
	}
	//fmt.Printf("%v\n", v)
}
