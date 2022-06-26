package main

import (
	"fmt"
	"io"
	"os"
	"reflect"
	"time"
)

type YourT1 struct {
	data string
}

func (y *YourT1) MethodBar() {
	fmt.Println(y.data + "MethodBar called")
}
func (y *YourT1) Metho1dBar() {
	fmt.Println(y.data + "MethodBar called")
}

type YourT2 struct {
	data string
}

func (y *YourT2) MethodFoo(i int, oo string) (bool, error) {
	a := y.data + "MethodFoo called"
	fmt.Println(a, i, oo)
	return true, nil
}

func InvokeObjectMethod(object interface{}, methodName string, args ...interface{}) (bool, error) {
	inputs := make([]reflect.Value, len(args))
	for i, _ := range args {
		inputs[i] = reflect.ValueOf(args[i])
	}
	call := reflect.ValueOf(object).MethodByName(methodName).Call(inputs)
	var err error
	if v, ok := call[1].Interface().(error); ok {
		err = v
	}
	return call[0].Bool(), err
}

type t struct {
	data string
}

func (t *t) Foo() {
	fmt.Println(t.data + "   foo")
}

//func MakeCommandMap() map[string]func(*YourT2) {
//	m := map[string]func(i int,oo string){
//		"USER": (*YourT2).MethodFoo,
//	}
//	// ...
//	return m
//}

func main() {
	//t := t{data: "vvvvvvvvv"}
	//reflect.ValueOf(&t).MethodByName("Foo").Call([]reflect.Value{})
	//name := "USER"
	//if fn := m[name]; fn != nil {
	//	fn(self) // where self is *Paradise
	//}
	t1 := &YourT2{data: "....2"}
	method, s := InvokeObjectMethod(t1, "MethodFoo", 1, "vv")
	fmt.Println(fmt.Sprint(method) + ":" + fmt.Sprint(s))
}

package main

import (
"fmt"
"io"
"os"
"reflect"
"time"
)

//import "time"

type Foo struct {
	Bar string `yaml:"manners"`
}

func test_main() {
	// As interface types are only used for static typing, a
	// common idiom to find the reflection Type for an interface
	// type Foo is to use a *Foo value.
	var teststring string
	fmt.Println(len(teststring))
	fmt.Println(time.Now().Format(time.RFC3339))

	writerType := reflect.TypeOf((*io.Writer)(nil)).Elem()

	fileType := reflect.TypeOf((*os.File)(nil))
	fmt.Println(fileType.Implements(writerType))
	f := Foo{"I want a tomato"}
	Say(f)
	// Output:
	// {I want a tomato}
	// Wanted output:
	// I want a tomato pretty please
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
