package main

import (
	"fmt"
	"strings"
)

func foo() *int {
	var x int
	return &x
}

func bar() int {
	x := new(int)
	*x = 1
	return *x
}

//go run -gcflags '-m -l' mem.go
func main() {
	foo()
	bar()
	println("over")

	path := "vvv"
	var args []string
	sprintf := fmt.Sprintf(`cmd path=%s,args=%s`, path, strings.Join(args, ","))
	fmt.Println(sprintf)
}
