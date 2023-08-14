package main

import (
	"bufio"
	"crypto/tls"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net"
	"os"
	"sync"
	"time"
)

type CollectInfos struct {
	ClientHellos []*tls.ClientHelloInfo
	sync.Mutex
}

var collectInfos CollectInfos
var currentClientHello *tls.ClientHelloInfo

func (c *CollectInfos) collectClientHello(clientHello *tls.ClientHelloInfo) {
	c.Lock()
	defer c.Unlock()
	c.ClientHellos = append(c.ClientHellos, clientHello)
}

func (c *CollectInfos) DumpInfo() {
	c.Lock()
	defer c.Unlock()
	data, err := json.Marshal(c.ClientHellos)
	if err != nil {
		log.Fatal(err)
	}
	ioutil.WriteFile("hello.json", data, os.ModePerm)
}

func getCert() *tls.Certificate {
	cert, err := tls.LoadX509KeyPair("server.pem", "server.key")
	if err != nil {
		log.Println(err)
		return nil
	}
	return &cert
}

func buildTlsConfig(cert *tls.Certificate) *tls.Config {
	cfg := &tls.Config{
		Certificates: []tls.Certificate{*cert},
		GetConfigForClient: func(clientHello *tls.ClientHelloInfo) (*tls.Config, error) {
			collectInfos.collectClientHello(clientHello)
			currentClientHello = clientHello
			return nil, nil
		},
	}
	return cfg
}

func serve(cfg *tls.Config) {
	ln, err := tls.Listen("tcp", ":443", cfg)
	if err != nil {
		log.Println(err)
		return
	}
	defer ln.Close()
	for {
		conn, err := ln.Accept()
		if err != nil {
			log.Println(err)
			continue
		}
		go handler(conn)
	}
}

func handler(conn net.Conn) {
	defer conn.Close()
	r := bufio.NewReader(conn)
	for {
		msg, err := r.ReadString('\n')
		if err != nil {
			log.Println(err)
			return
		}
		fmt.Println(msg)
		data, err := json.Marshal(currentClientHello)
		if err != nil {
			log.Fatal(err)
		}
		_, err = conn.Write(data)
		if err != nil {
			log.Println(err)
			return
		}
	}
}

func main() {
	go func() {
		for {
			collectInfos.DumpInfo()
			time.Sleep(10 * time.Second)
		}
	}()
	cert := getCert()
	if cert != nil {
		serve(buildTlsConfig(cert))
	}
}
