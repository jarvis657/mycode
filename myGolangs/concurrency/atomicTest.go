package main

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"strings"
)

func main() {

	url := "https://api.writesonic.com/v2/business/content/chatsonic?engine=premium"

	s := "{\"enable_google_results\":\"true\",\"enable_memory\":true,\"input_text\":\"What's the weather like today?\"}"
	fmt.Println(s)
	payload := strings.NewReader(s)

	req, _ := http.NewRequest("POST", url, payload)

	req.Header.Add("accept", "application/json")
	req.Header.Add("content-type", "application/json")
	req.Header.Add("X-API-KEY", "a1795d28-a874-4d8d-9ef9-c2183a9e3e4f")

	res, _ := http.DefaultClient.Do(req)

	defer res.Body.Close()
	body, _ := ioutil.ReadAll(res.Body)

	fmt.Println(res)
	fmt.Println(string(body))

}

//
//var accountSeq = make(map[int]int32)
//
//func testMapIncr() {
//
//	for i := 0; i < 3; i++ {
//		go func(i int) {
//	        异常不能这么写t
//			atomic.AddInt32(&accountSeq[i], 1)
//		}(i)
//	}
//}
//
//func initMap() {
//	accountSeq[0] = new(0)
//	accountSeq[1] = 0
//	accountSeq[2] = 0
//	accountSeq[3] = 0
//}
