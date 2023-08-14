package main

import (
	"crypto/tls"
	"fmt"
	"log"
)

func main() {
	conn, err := tls.Dial("tcp", "localhost:443", &tls.Config{InsecureSkipVerify: true})
	if err != nil {
		log.Fatal(err)
	}
	defer conn.Close()
	_, err = conn.Write([]byte("hello\n"))
	if err != nil {
		log.Fatal(err)
	}
	buf := make([]byte, 1000)
	n, err := conn.Read(buf)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println(string(buf[:n]))
}
