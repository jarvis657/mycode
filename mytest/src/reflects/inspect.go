package main

import (
	"fmt"
	"reflect"
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

func main() {
	f := &Foo{
		FirstName: "Drew",
		LastName:  "Olson",
		Age:       30,
	}
	a := inspect(f)
	dump(a)
}
