package main

import (
	"fmt"
	"reflect"
	"time"
)

type myStruct struct {
}
type TestError struct {
}

func (te *TestError) Error() string {
	return "error happened"
}
func testError() *TestError {
	return nil
}
func innerError() error {
	return nil
}

func mystruct() *myStruct {
	return nil
}

func test() error {
	var err error
	err = testError() //如果此处修改为return nil，结果会return true 符合预期
	//if err != nil {
	//	return err
	//}
	//return nil
	return err
}
func modifySlice(innerSlice []*string) {
	a := "a"
	b := "b"
	innerSlice = append(innerSlice, &a)
	(innerSlice)[0] = &b
	(innerSlice)[1] = &b
	fmt.Println(innerSlice)
}

//func print[T any](arr []T) {
//	for _, v := range arr {
//		fmt.Print(v)
//		fmt.Print(" ")
//	}
//	fmt.Println("")
//}
type Shape interface {
	Sides() int
	Area() int
}
type Square struct {
	len int
}

func (s *Square) Sides() int {
	return 4
}

//func (s *Square) Area() int {
//	return 4
//}

//问题是如果是返回的是接口类型 内部可能发生转化 导致 外面调用的 == nil 本期望是nil 结果发生强制转化 导致  判断是false
func main() {
	sq := Square{len: 5}
	fmt.Printf("%d\n", sq.Sides())
	strs := []string{"Hello", "World", "Generics"}
	decs := []float64{3.14, 1.14, 1.618, 2.718}
	nums := []int{2, 4, 6, 8}

	print(strs)
	print(decs)
	print(nums)

	fmt.Println(fmt.Sprintf("相同版本:%v%%,异常问题:%v", 12.1, "aa"))
	ls := []int{1, 2, 3, 4, 5, 6, 7}
	for _, i := range ls {
		for j := 0; j < 6; j++ {
			if j > 2 {
				break
			}
			fmt.Printf("i:%v,j:%v\n", i, j)
		}
	}
	createElemDuringIterMap()
	subs := "1020451174867996223"
	fmt.Println(subs[len(subs)-9:])
	s := []rune("世界世界世❤️界")
	first3 := string(s[0:3])
	last3 := string(s[len(s)-3:])
	fmt.Println(len(s))
	fmt.Println(first3)
	fmt.Println(last3)
	fmt.Println(subs)

	nz, e3 := time.LoadLocation("America/New_York")
	fmt.Println(e3)
	parse, e3 := time.ParseInLocation(time.RFC3339, "2021-07-14T06:37:33Z", nz)
	unix := parse.In(nz).Unix()
	zone, offset := parse.Zone()
	fmt.Printf("nz:%v,offset:%v,parse:%v,unix:%v,err:%v\n", zone, offset, parse, unix, e3)

	fmt.Println("=====================")

	nz, e3 = time.LoadLocation("Asia/Shanghai")
	fmt.Println(e3)
	parse, e3 = time.ParseInLocation(time.RFC3339, "2021-07-14T06:37:33Z", nz)
	zone, offset = parse.In(nz).Zone()
	unix = parse.In(nz).Unix()
	fmt.Printf("nz:%v,offset:%v,parse:%v,unix:%v,err:%v\n", zone, offset, parse, unix, e3)

	//设置时区东八区
	NowTimeZone := time.FixedZone("CST", 8*3600)
	//timeUnix:=time.Now().Unix()//获取时间戳 1588087670
	//fmt.Println(timeUnix)
	//formatTimeStr:=time.Now().In(NowTimeZone).Format(time.RFC3339)//必须这样的格式612345
	parse, err3 := time.Parse(time.RFC3339, "2021-07-12T12:34:55Z")
	format := parse.In(NowTimeZone).Format("2006-01-02 15:04:05")
	fmt.Println(format, err3) //打印结果：2020-04-28 23:27:50

	err := test()
	fmt.Println("err", err == nil || reflect.ValueOf(err).IsNil())
	fmt.Println(err == nil) //输出 false,重现了问题
	fmt.Printf("err:%+v\n", err)
	err2 := innerError()
	fmt.Println(err2 == nil)
	fmt.Printf("err2:%+v\n", err2)
	fmt.Println("===============")
	m := mystruct()
	fmt.Println(m == nil)
	fmt.Printf("struct:%+v\n", m)

	//mm:=make(map[int]string)
	//mm[1]="a"
	//for i := 0; i < 50; i++ {
	//	//some line will not show 44, some line will
	//	createElemDuringIterMap()
	//	fmt.Println("===============")
	//}
}

var createElemDuringIterMap = func() {
	var m = map[int]int{1: 1, 2: 2, 3: 3}
	for i := range m {
		m[4] = 4
		fmt.Printf("%d%d ", i, m[i])
	}
}

func isNilFixed(i interface{}) bool {
	if i == nil {
		return true
	}
	switch reflect.TypeOf(i).Kind() {
	case reflect.Ptr, reflect.Map, reflect.Array, reflect.Chan, reflect.Slice:
		return reflect.ValueOf(i).IsNil()
	}
	return false
}
