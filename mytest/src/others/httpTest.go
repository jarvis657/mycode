package main

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"runtime"
)

func main() {
	HttpGet()
}

type SS struct {
	name string
}

func HttpGet() {
	testMap := make(map[int][]*SS)
	ss := &SS{name: "aaa"}
	testMap[1] = append(testMap[1], ss)
	for _, ts := range testMap {
		for _, t := range ts {
			fmt.Println(t)
		}
	}

	for {
		fmt.Println("new")
		resp, err := http.Get("http://d.jd.com")
		if err != nil {
			fmt.Println(err)
			continue
		}
		if http.StatusOK == resp.StatusCode {
			//如果不加这个 则goroutine会泄漏
			ioutil.ReadAll(resp.Body)
			//fmt.Println(string(all))
			resp.Body.Close()
			fmt.Println("ok go sum:", runtime.NumGoroutine())
			continue
		}
		err = resp.Body.Close()
		if err != nil {
			return
		}
		fmt.Println("go sum:", runtime.NumGoroutine())
	}
}
