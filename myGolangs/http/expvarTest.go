package main

import (
	"expvar"
	"fmt"
	"log"
	"net/http"
	"runtime"

	//漏桶算法
	"go.uber.org/ratelimit"

	//令牌桶算法
	"golang.org/x/time/rate"
)

func kvFunc(kv expvar.KeyValue) {
	fmt.Println(kv.Key, kv.Value)
}

func main() {
	runtime.GOMAXPROCS(16)
	ratelimit.New(10).Take().Add(10)
	limiter := rate.Limiter{}
	limiter.Limit()
	inerInt := int64(10)
	pubInt := expvar.NewInt("Int")
	pubInt.Set(inerInt)
	pubInt.Add(2)

	inerFloat := 1.2
	pubFloat := expvar.NewFloat("Float")
	pubFloat.Set(inerFloat)
	pubFloat.Add(0.1)

	inerString := "hello"
	pubString := expvar.NewString(inerString)
	pubString.Set(inerString)

	pubMap := expvar.NewMap("Map").Init()
	pubMap.Set("Int", pubInt)
	pubMap.Set("Float", pubFloat)
	pubMap.Set("String", pubString)
	pubMap.Do(kvFunc)
	pubMap.Add("Int", 1)
	pubMap.Add("NewInt", 123)
	pubMap.AddFloat("Float", 0.5)
	pubMap.AddFloat("NewFloat", 0.9)
	pubMap.Do(kvFunc)

	expvar.Do(kvFunc)
	http.HandleFunc(
		"/", func(w http.ResponseWriter, r *http.Request) {
			fmt.Fprintf(w, "hello")
		},
	)
	err := http.ListenAndServe(":8080", nil)
	if err != nil {
		log.Fatalln(err)
	}
}
