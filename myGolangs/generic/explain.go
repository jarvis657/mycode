package main

import "fmt"

type CommonType[T int | string | float32] []T

// 错误
// type CommonType2[T int | string | float32] T

// 一个泛型类型的结构体。可用 int 或 sring 类型实例化
type MyStruct[T int | string] struct {
	Name string
	Data T
}

// 一个泛型接口(关于泛型接口在后半部分会详细讲解）
type IPrintData[T int | float32 | string] interface {
	Print(data T)
}

// 一个泛型通道，可用类型实参 int 或 string 实例化
type MyChan[T int | string] chan T

type NewType[T interface{ *int }] []T

type NewType2[T interface{ *int | *float64 }] []T

// 如果类型约束中只有一个类型，可以添加个逗号消除歧义
type NewType3[T *int,] []T

// 先定义个泛型类型 Slice[T]
type Slice[T int | string | float32 | float64 | uint | uint8] []T

type UintSlice[T uint | uint8] Slice[T]

// ✓ 正确。基于泛型类型Slice[T]定义了新的泛型类型 FloatSlice[T] 。FloatSlice[T]只接受float32和float64两种类型
type FloatSlice[T float32 | float64] Slice[T]

// ✓ 正确。基于泛型类型Slice[T]定义的新泛型类型 IntAndStringSlice[T]
type IntAndStringSlice[T int | string] Slice[T]

type MySlice[T int | float32] []T

func (s MySlice[T]) Sum() T {
	var sum T
	for _, value := range s {
		sum += value
	}
	return sum
}
func (s MySlice[T]) Add(a, b T) T {
	return a + b
}

// 泛型函数
func Add[T int | float32 | float64](a T, b T) T {
	return a + b
}

func MyFunc[T int | float32 | float64](a, b T) {
	// 匿名函数可使用已经定义好的类型形参
	fn2 := func(i T, j T) T {
		return i*2 - j*2
	}
	fn2(a, b)
}

//  目前错误 不支持泛型方法
//func (receiver A) Add[T int | float32 | float64](a T, b T) T {
//	return a + b
//}

// 接口:从方法集(Method set)到类型集(Type set)
// ~底层是float32或者float64的类型
// 使用 ~ 时有一定的限制：
// ~后面的类型不能为接口
// ~后面的类型必须为基本类型
type Float interface {
	~float32 | ~float64
}

type SliceMy[T Float] []T

// 类型并集
type AllInt interface {
	~int | ~int8 | ~int16 | ~int32 | ~int64 | ~uint | ~uint8 | ~uint16 | ~uint32
}
type Uint interface {
	~uint | ~uint8 | ~uint16 | ~uint32 | ~uint64
}
type A interface { // 接口A代表的类型集是 AllInt 和 Uint 的交集
	AllInt
	Uint
}

type ReadWriter interface {
	~string | ~[]rune

	Read(p []byte) (n int, err error)
	Write(p []byte) (n int, err error)
}

// 类型 StringReadWriter 实现了接口 Readwriter
type StringReadWriter string

func (s StringReadWriter) Read(p []byte) (n int, err error) {
	// ...
	return 0, nil
}

func (s StringReadWriter) Write(p []byte) (n int, err error) {
	// ...
	return 0, nil
}

// 类型BytesReadWriter 没有实现接口 Readwriter
type BytesReadWriter []byte

func (s BytesReadWriter) Read(p []byte) (n int, err error) {
	return 0, nil
}

func (s BytesReadWriter) Write(p []byte) (n int, err error) {
	return 0, nil
}

// 不能这样
//
//	func Test(writer ReadWriter) {
//		writer.Read(nil)
//	}
//
// 只能作为泛型参数,不能用作类型声明和类型参数-对于embed interface来说
func Test[T ReadWriter](writer T) {
	writer.Read(nil)
}

// 重点！！！！！只能做泛型参数或者embed 到其他interface中 -对于embed interface来说
// 只有实现了 Process(string) string 和 Save(string) error 这两个方法，并且以 int 或 struct{ Data interface{} } 为底层类型的类型才算实现了这个接口
// 不能用于变量定义只能用于类型约束，所以接口 DataProcessor2[T] 只是定义了一个用于类型约束的类型集
type DataProcessorType[T any] interface {
	int | ~struct{ Data interface{} } //说明这是embed interface

	Process(data T) (newData T)
	Save(data T) error
}

type DataProcessor[T any] interface {
	Process(oriData T) (newData T)
	Save(data T) error
}
type CSVProcessor struct {
}

// 注意，方法中 oriData 等的类型是 string
func (c CSVProcessor) Process(oriData string) (newData string) {
	return ""
}

func (c CSVProcessor) Save(oriData string) error {
	return nil
}

// XMLProcessor 虽然实现了接口 DataProcessor2[string] 的两个方法，但是因为它的底层类型是 []byte，所以依旧是未实现 DataProcessor2[string]
type XMLProcessor []byte

func (c XMLProcessor) Process(oriData string) (newData string) {
	return ""
}

func (c XMLProcessor) Save(oriData string) error {
	return nil
}

// JsonProcessor 实现了接口 DataProcessor2[string] 的两个方法，同时底层类型是 struct{ Data interface{} }。所以实现了接口 DataProcessor2[string]
type JsonProcessor struct {
	Data interface{}
}

func (c JsonProcessor) Process(oriData string) (newData string) {
	return ""
}

func (c JsonProcessor) Save(oriData string) error {
	return nil
}

// 错误。DataProcessor2[string]是一般接口不能用于创建变量
//var processor DataProcessor2[string]

// 正确，实例化之后的 DataProcessor2[string] 可用于泛型的类型约束
type ProcessorList[T DataProcessorType[string]] []T

// 正确，接口可以并入其他接口
//type StringProcessor interface {
//	DataProcessor2[string]
//
//	PrintString()
//}

// 错误，带方法的一般接口不能作为类型并集的成员(参考6.5 接口定义的种种限制规则
//
//	type StringProcessor interface {
//		DataProcessor2[string] | DataProcessor2[[]byte]
//
//		PrintString()
//	}
type Numeric interface {
	int | int8 | int16 | int32 | int64 | uint | uint8 | uint16 | uint32 | uint64 | float32 | float64
}

func min[T Numeric](a, b T) T {
	if a < b {
		return a
	}
	return b
}

type Stack[T any] []T

func (s *Stack[T]) Push(v T) {
	*s = append(*s, v)
}

func (s *Stack[T]) Pop() T {
	if len(*s) == 0 {
		var zero T
		//return nil
		return zero
	}
	t := (*s)[len(*s)-1]
	*s = (*s)[:len(*s)-1]
	return t
}

func (s *Stack[T]) Pop2() (t T) {
	if len(*s) == 0 {
		return
	}
	t = (*s)[len(*s)-1]
	*s = (*s)[:len(*s)-1]
	return t
}

func Filter[T any](f func(T) bool, src []T) []T {
	var dst []T
	for _, v := range src {
		if f(v) {
			dst = append(dst, v)
		}
	}
	return dst
}

func Foo[T any](n T) {
	//将 T 转化为 interface{}，然后做一次 type assertion
	if _, ok := (interface{})(n).(int); ok {
	}
}

type Int interface {
	~int | ~uint
}

func IsSigned[T Int](n T) {
	switch (interface{})(n).(type) {
	case int:
		fmt.Println("signed")
	default:
		fmt.Println("unsigned")
	}
}

type MyInt int

// IsSigned(1)
// IsSigned(MyInt(1))
// Output:
// signed
// unsigned

//type Signed interface {
//	~int
//}
//
//func IsSigned2[T Int](n T) {
//	if _, ok := (interface{})(n).(Signed); ok { 这里会有异常
//		fmt.Println("signed")
//	} else {
//		fmt.Println("unsigned")
//	}
//}

type Aa struct {
	A string
	B string
}

func main() {
	IsSigned(1)
	IsSigned(MyInt(1))
	src := []int{-2, -1, -0, 1, 2}
	dst := Filter(func(v int) bool { return v >= 0 }, src)
	fmt.Println(dst)

	Add[int](1, 2)
	m := MySlice[int]{1, 2, 4}
	m.Sum()

	var processor DataProcessor[string] = CSVProcessor{}
	processor.Process("name,age\nbob,12\njack,30")
	err := processor.Save("name,age\nbob,13\njack,31")
	if err != nil {
		return
	}

	var jsonProcessor DataProcessor[string] = JsonProcessor{Data: "haha"}
	jsonProcessor.Process("aa")
}
