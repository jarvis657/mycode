package main

import (
	"fmt"
	"sort"
	"strings"
)

func main() {
	colors := strings.Fields(`black white red orange yellow green blue indigo violet`)
	//sort.Sort(ByLen(colors))
	sort.Sort(sort.StringSlice(colors))
	fmt.Println(colors)
}
