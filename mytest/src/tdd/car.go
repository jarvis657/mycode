package main

import (
	"fmt"

	"go.starlark.net/starlark"
)

//方向
var E = [2]int32{1, 0}
var N = [2]int32{0, 1}
var W = [2]int32{-1, 0}
var S = [2]int32{0, -1}

var events = make([][2]int32, 0)

var current_Turn [2]int32

type Car struct {
	x int32
	y int32
}

//receiveEvent 接受指令
func receiveEvent(t []string) [][2]int32 {
	return events
}

//initCar 初始化car
func initCar(car Car, x int32, y int32) *Car {
	return nil
}

// moveCar 移动
func moveCar(car Car, move [2]int32) *Car {
	return nil
}

//当前信息
func info(car Car) *[2]int32 {
	return nil
}

// 转向
func turn(t string) *[2]int32 {
	switch t {
	case "E":
		return &E
	case "N":
		return &N
	case "W":
		return &W
	case "S":
		return &S
	}
	return nil
}

//func main() {
//	thread := &starlark.Thread{
//		Name: "starlark",
//		Print: func(_ *starlark.Thread, msg string) {
//			// starlark.Thread 是执行栈
//			log.Println("go log print:%v", msg)
//		},
//		Load: func(thread *starlark.Thread, module string) (starlark.StringDict, error) {
//			log.Println("go load print:%v", module)
//			return nil,nil
//		},
//	}
//	g, err := starlark.ExecFile(thread, "", `print("hello starlark!")`, nil)
//	if err != nil {
//		log.Fatalln(err)
//	}
//	// g 是脚本的全局对象字典
//	log.Println(g)
//}

//preload
//func Test(_ *starlark.Thread, _ *starlark.Builtin, _ starlark.Tuple, _ []starlark.Tuple) (starlark.Value, error) {
//	log.Println("Test.............golang")
//	return starlark.None, nil
//}
//
//func main() {
//	thread := &starlark.Thread{
//		Name: "starlark",
//		Print: func(_ *starlark.Thread, msg string) {
//			// starlark.Thread 是执行栈
//			log.Println(msg)
//		},
//	}
//	// 手动构建 starlarkstruct.Module 即可
//	ctxModule := &starlarkstruct.Module{
//		Name: "ctx",
//		Members: starlark.StringDict{
//			"test": starlark.NewBuiltin("test", Test),
//		},
//	}
//	predeclared := starlark.StringDict{"ctx": ctxModule}
//	g, err := starlark.ExecFile(thread, "", `ctx.test()`, predeclared)
//	if err != nil {
//		log.Fatalln(err)
//	}
//	// g 是脚本的全局对象字典
//	log.Println(g)
//}

// callback
//func main() {
//	thread := &starlark.Thread{
//		Name: "starlark",
//		Print: func(_ *starlark.Thread, msg string) {
//			// starlark.Thread 是执行栈
//			log.Println(msg)
//		},
//	}
//	g, err := starlark.ExecFile(thread, "", `def call(msg): print(msg)`, nil)
//	if err != nil {
//		log.Fatalln(err)
//	}
//	// g 是脚本的全局对象字典
//	_, err = starlark.Call(thread, g["call"], []starlark.Value{starlark.String("call ok")}, nil)
//	if err != nil {
//		log.Fatalln(err)
//	}
//}
// callback

//func Test(t_ *starlark.Thread, b_ *starlark.Builtin, args starlark.Tuple, ts_ []starlark.Tuple) (starlark.Value, error) {
//	log.Printf("Test:%v",args[0].(starlark.String).GoString())
//	return starlark.None, nil
//}
//
//func main() {
//	thread := &starlark.Thread{
//		Name: "starlark",
//		Print: func(_ *starlark.Thread, msg string) {
//			// starlark.Thread 是执行栈
//			log.Println(msg)
//		},
//	}
//	g, err := starlark.ExecFile(
//		thread, "", `
//def deep_call(call2):
//    call2("call go")
//    print("print haha")`, nil,
//	)
//	if err != nil {
//		log.Fatalln(err)
//	}
//	// g 是脚本的全局对象字典
//	_, err = starlark.Call(thread, g["deep_call"], []starlark.Value{starlark.NewBuiltin("callBuiltin", Test)}, nil)
//	if err != nil {
//		log.Fatalln(err)
//	}
//}

//func error_(thread *starlark.Thread, _ *starlark.Builtin, args starlark.Tuple, kwargs []starlark.Tuple) (starlark.Value, error) {
//	if len(args) != 1 {
//		return nil, fmt.Errorf("error: got %d arguments, want 1", len(args))
//	}
//	buf := new(strings.Builder)
//	stk := thread.CallStack()
//	stk.Pop()
//	_, _ = fmt.Fprintf(buf, "%sError: ", stk)
//	if s, ok := starlark.AsString(args[0]); ok {
//		buf.WriteString(s)
//	} else {
//		buf.WriteString(args[0].String())
//	}
//	return starlark.None, fmt.Errorf(buf.String())
//}
//
//
//func main() {
//	thread := &starlark.Thread{
//		Name: "starlark",
//		Print: func(_ *starlark.Thread, msg string) {
//			// starlark.Thread 是执行栈
//			log.Println(msg)
//		},
//	}
//	predeclared := starlark.StringDict{
//		"error": starlark.NewBuiltin("error", error_),
//	}
//	_, err := starlark.ExecFile(thread, "", `
//def main():
//    error("1")
//main()
//    `, predeclared)
//	if err != nil {
//		log.Fatalln(err)
//	}
//}

func main() {
	// Execute Starlark program in a file.
	thread := &starlark.Thread{Name: "my thread"}
	globals, err := starlark.ExecFile(
		thread, "", `def fibonacci(n):
    res = list(range(n))
    for i in res[2:]:
        res[i] = res[i-2] + res[i-1]
    return res
`, nil,
	)
	if err != nil {
	}

	// Retrieve a module global.
	fibonacci := globals["fibonacci"]

	// Call Starlark function from Go.
	v, err := starlark.Call(thread, fibonacci, starlark.Tuple{starlark.MakeInt(100)}, nil)
	if err != nil {
	}
	fmt.Printf("fibonacci(10) = %v\n", v) // fibonacci(10) = [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
}
