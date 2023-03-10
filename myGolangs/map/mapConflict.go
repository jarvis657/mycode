package main

import (
	"fmt"
	"sort"
)

func sortValue() {
	basket := map[string]int{"orange": 5, "apple": 7,
		"mango": 3, "strawberry": 9}

	keys := make([]string, 0, len(basket))

	for key := range basket {
		keys = append(keys, key)
	}

	fmt.Println(basket)
	fmt.Println(keys)

	sort.SliceStable(keys, func(i, j int) bool {
		return basket[keys[i]] < basket[keys[j]]
	})

	fmt.Println(keys)
}
func sortKey() {
	basket := map[string]int{"orange": 5, "apple": 7,
		"mango": 3, "strawberry": 9}

	keys := make([]string, 0, len(basket))
	for key := range basket {
		keys = append(keys, key)
	}

	fmt.Println(basket)
	fmt.Println(keys)

	sort.SliceStable(keys, func(i, j int) bool {
		return basket[keys[i]] < basket[keys[j]]
	})
	//sort.Sort(sort.Reverse(sort.StringSlice(keys)))

	fmt.Println(keys)
}

func main() {
	sortValue()
	c := make(chan bool)
	m := make(map[string]string)
	go func() {
		m["1"] = "a" // First conflicting access.
		c <- true
	}()
	m["2"] = "b" // Second conflicting access.
	<-c
	for k, v := range m {
		fmt.Println(k, v)
	}

}
