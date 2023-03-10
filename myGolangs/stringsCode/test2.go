package main

import (
	"crypto/md5"
	"encoding/base64"
	"encoding/hex"
	"fmt"
	"os"
	"sort"
	"strings"
	"sync"
	"time"
)

var requestDataDetails = sync.Map{}

func main() {

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

	dir, _ := os.Executable()
	fmt.Println(dir)
	fmt.Println(os.Getpid())
	hash := GetMD5Hash(`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`)
	fmt.Println(hash)
	h := md5.New()
	h.Write([]byte(`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`))
	fmt.Println(base64.StdEncoding.EncodeToString(h.Sum(nil)))
	trim := strings.Trim("\n\n", "\n")
	fmt.Println(len(trim))
	fmt.Println(trim)
	fmt.Println(time.Now().Unix())
	requestDataDetails.Store("abc", "hhhhhhh")
	value, ok := requestDataDetails.Load("abc")
	p(value.(string))
	fmt.Printf("value:%v,err:%v\n", value, ok)
	requestDataDetails.Store("abc", "xxxxxx")
	value, ok = requestDataDetails.Load("abc")
	fmt.Println("================================")
	fmt.Printf("value:%v,err:%v\n", value, ok)
}
func p(a string) {
	fmt.Println(a)
}

func GetMD5Hash(text string) string {
	hash := md5.Sum([]byte(text))
	return hex.EncodeToString(hash[:])
}
