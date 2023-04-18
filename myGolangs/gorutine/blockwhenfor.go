package main

import (
	"fmt"
	"io"
	"log"
	"net/http"
	"runtime"
	"time"
)

func main() {
	runtime.GOMAXPROCS(runtime.NumCPU())
	go server()
	//go printNum()
	//var i = 1
	//for {
	//	// will block here, and never go out
	//	i++
	//}
	//fmt.Println("for loop end")
	time.Sleep(time.Second * 36000)
}

func printNum() {
	i := 0
	for {
		fmt.Println(i)
		i++
	}
}

func HelloServer(w http.ResponseWriter, req *http.Request) {
	println(".........................hello server")
	time.Sleep(time.Minute * 5)
	io.WriteString(w, "hello, world!\n")
}

func server() {
	http.HandleFunc("/haha", HelloServer)
	err := http.ListenAndServe("127.0.0.1:7777", nil)

	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
	var mmap = make(map[string]string, 10)
	mmap["s"] = "2"
	println(len(mmap))
}
