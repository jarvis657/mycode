package main

import (
	"fmt"
	"reflect"

	"git.code.oa.com/trpc-go/trpc-go/errs"
)

type Container struct {
	s reflect.Value
}

func NewContainer(t reflect.Type, size int) *Container {
	if size <= 0 {
		size = 64
	}
	return &Container{
		s: reflect.MakeSlice(reflect.SliceOf(t), 0, size),
	}
}
func (c *Container) Put(val interface{}) error {
	if reflect.ValueOf(val).Type() != c.s.Type().Elem() {
		return fmt.Errorf("Put: cannot put a %T into a slice of %s", val, c.s.Type().Elem())
	}
	c.s = reflect.Append(c.s, reflect.ValueOf(val))
	return nil
}
func (c *Container) Get(refval interface{}) error {
	if reflect.ValueOf(refval).Kind() != reflect.Ptr || reflect.ValueOf(refval).Elem().Type() != c.s.Type().Elem() {
		return fmt.Errorf("Get: needs *%s but got %T", c.s.Type().Elem(), refval)
	}
	reflect.ValueOf(refval).Elem().Set(c.s.Index(0))
	c.s = c.s.Slice(1, c.s.Len())
	return nil
}

func main_test() {
	f1 := 3.1415926
	f2 := 1.41421356237
	c := NewContainer(reflect.TypeOf(f1), 16)
	if err := c.Put(f1); err != nil {
		panic(err)
	}
	if err := c.Put(f2); err != nil {
		panic(err)
	}
	g := 0.0
	if err := c.Get(&g); err != nil {
		panic(err)
	}
	fmt.Printf("%v (%T)\n", g, g) //3.1415926 (float64)
	fmt.Println(c.s.Index(0))     //1.4142135623
}

func InArray(x interface{}, xs interface{}) (bool, error) {
	xRt := reflect.TypeOf(x)
	xsRt := reflect.TypeOf(xs)
	containerType := xsRt.Kind()
	eType := xsRt.Elem().Kind()
	rv := reflect.ValueOf(xs)
	if containerType == reflect.Ptr {
		containerType = xsRt.Elem().Kind()
		eType = xsRt.Elem().Elem().Kind()
		rv = rv.Elem()
	}
	if containerType != reflect.Slice && containerType != reflect.Array {
		return false, errs.New(-1, "containers type error")
	}
	if xRt.Kind() != eType {
		return false, errs.New(-1, "err elem type error")
	}
	for i := 0; i < rv.Len(); i++ {
		b := x == rv.Index(i).Interface()
		if b {
			return true, nil
		}
	}
	return false, nil
}

type Student struct {
	Name string
	Age  int
}

func main() {
	x := "a"
	var xs [2]string
	xs[1]="a"
	xs[0]="b"
	array, err := InArray(x, xs)
	fmt.Println(array, err)
}
