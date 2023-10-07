package main

import (
	"log"

	"github.com/dengsgo/go-decorator/decor"
)

func main() {
	// 正常调用你的函数。
	// 由于这是一个声明使用装饰器logging的函数,
	// decorator 编译链会在编译代码时注入装饰器方法logging的调用。
	// 所以使用上面的方式编译后运行，你会得到如下输出：
	//
	// 2023/08/13 20:26:30 decorator function logging in []
	// 2023/08/13 20:26:30 this is a function: myFunc
	// 2023/08/13 20:26:30 decorator function logging out []
	//
	// 而不是只有 myFunc 本身的一句输出。
	// 也就是说通过装饰器改变了这个方法的行为！
	myFunc()
}

// 通过使用 go:decor 注释声明该函数将使用装饰器logging来装饰。
//
//go:decor logging
func myFunc() {
	log.Println("this is a function: myFunc")
}

// 这是一个普通的函数
// 但是它实现了 func(*decor.Context) 类型，因此它还是一个装饰器方法，
// 可以在其他函数上使用这个装饰器。
// 在函数中，ctx 是装饰器上下文，可以通过 ctx 获取到目标函数的出入参
// 和目标方法的执行。
// 如果函数中没有执行 ctx.TargetDo(), 那么意味着目标函数不会执行，
// 即使你代码里调用了被装饰的目标函数！这时候，目标函数返回的都是零值。
// 在 ctx.TargetDo() 之前，可以修改 ctx.TargetIn 来改变入参值。
// 在 ctx.TargetDo() 之后，可以修改 ctx.TargetOut 来改变返回值。
// 只能改变出入参的值。不要试图改变他们的类型和数量，这将会引发运行时 panic !!!
func logging(ctx *decor.Context) {
	log.Println("decorator function logging in", ctx.TargetIn)
	ctx.TargetDo()
	log.Println("decorator function logging out", ctx.TargetOut)
}
