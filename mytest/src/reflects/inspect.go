package main

import (
	"fmt"
	"io"
	"os"
	"reflect"
	"time"
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

//func main() {
//	f := &Foo{
//		FirstName: "Drew",
//		LastName:  "Olson",
//		Age:       30,
//	}
//	a := inspect(f)
//	dump(a)
//}

func main() {
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
