package main

import (
	"fmt"
	"os"
	"reflect"
	"regexp"
)

type Point struct {
	Longitude     interface{}
	Latitude      interface{}
	Distance      interface{}
	ElevationGain interface{}
	ElevationLoss interface{}
}

func parse() (*Point, error) {
	var p Point
	var err error
	read := func(data interface{}) {
		if err != nil {
			return
		}
		//err = binary.Read(r, binary.BigEndian, data)
		err = fmt.Errorf("aaa%v", "hha")
	}
	read(&p.Longitude)
	read(&p.Latitude)
	read(&p.Distance)
	read(&p.ElevationGain)
	read(&p.ElevationLoss)
	if err != nil {
		return &p, err
	}
	return &p, nil
}

func main() {
	aaa := []int32{int32(3), int32(4), int32(5), int32(6), int32(7)}
	for i := range aaa {
		fmt.Println(i)
	}

	point, err := parse()
	fmt.Println(point, err)
	re := regexp.MustCompile("\\$\\{(.*?)\\}")
	match := re.FindStringSubmatch("git commit -m '${abc}'")
	fmt.Println(match[1])

	command := "git commit -m '${abc}'"
	var match1 string
	os.Expand(
		command, func(s string) string {
			match1 = s
			return ""
		},
	)
	println(match1 == "abc")

	targetIds := []int32{int32(1), int32(2), int32(3), int32(4), int32(5)}
	in := In(1, targetIds)
	ss := []string{"a", "b", "c"}
	b := In("a", ss)
	fmt.Printf("ss:%+v,b:%+v", in, b)
}

func In(target interface{}, array interface{}) bool {
	//of := reflect.TypeOf(array)
	value := reflect.ValueOf(target)
	valueArray := reflect.ValueOf(array)
	fmt.Printf("of:%v\n", value)
	fmt.Printf("of:%v\n", valueArray)

	switch target.(type) {
	case int32, int, int64, int8:
		for _, element := range array.([]int32) {
			if target.(int32) == element {
				return true
			}
		}
	case string:
		for _, element := range array.([]string) {
			if target == element {
				return true
			}
		}
	}
	return false
}
