package main

import (
	"fmt"
	"io"
	"log"
	"net"
	"net/http"
	"net/url"
	"strings"
	"time"
)

type MyServer struct {
	i int
}

// 接收者的类型才是真正实现接口的类型
func (myServer *MyServer) ServeHTTP(w http.ResponseWriter, request *http.Request) {
	io.WriteString(w, "Hello")
	fmt.Println(request.UserAgent())
}
func newTransport(proxy string) *http.Client {
	h := &http.Client{
		Timeout: time.Duration(600) * time.Second,
	}
	dialer := net.Dialer{
		Timeout:   5 * time.Second,
		Deadline:  time.Now().Add(3 * time.Second),
		KeepAlive: 5 * time.Second,
	}
	h.Transport = &http.Transport{
		DialContext:         dialer.DialContext,
		Proxy:               http.ProxyFromEnvironment,
		IdleConnTimeout:     time.Duration(30) * time.Second,
		TLSHandshakeTimeout: 10 * time.Second,
	}
	if strings.TrimSpace(proxy) != "" {
		proxyUrl, err := url.Parse(proxy)
		if err != nil {
			fmt.Println("[api.newTransport]newTransport error: %s", err.Error())
			return nil
		}
		h.Transport = &http.Transport{
			DialContext:         dialer.DialContext,
			IdleConnTimeout:     time.Duration(30) * time.Second,
			Proxy:               http.ProxyURL(proxyUrl),
			TLSHandshakeTimeout: 10 * time.Second,
		}
	} else {
		h.Transport = &http.Transport{
			DialContext:         dialer.DialContext,
			IdleConnTimeout:     time.Duration(30) * time.Second,
			TLSHandshakeTimeout: 10 * time.Second,
		}
	}
	return h
}
func main() {
	myServer := MyServer{1}
	err := http.ListenAndServe(":8080", &myServer) //接收者的类型才是真正实现接口的类型
	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
}
