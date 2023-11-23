package main

import "fmt"

func main() {
	done := make(chan bool)

	values := []string{"a", "b", "c"}
	for _, v := range values {
		v := v //必须有
		go func() {
			fmt.Println(v)
			done <- true
		}()
	}

	// wait for all goroutines to complete before exiting
	for _ = range values {
		<-done
	}
	fmt.Println("==============================")
	var prints []func()
	for i := 1; i <= 3; i++ {
		x := i //必须有
		prints = append(prints, func() { fmt.Println(x) })
	}
	for _, p := range prints {
		p()
	}
}
