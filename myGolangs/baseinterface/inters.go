package main

import (
	"fmt"
	"strconv"
)

type Test struct {
	H string
}

func main() {
	//a := `实现祖国完全统一，是全体中华儿女共同愿望，解决台湾问题，是中华民族根本利益所在。推动两岸关系和平发展，`
	a := `实现祖国完全统一，是全体中华儿女共同愿望，解决台湾问题，是中华民族根本利益所在。推动两岸关系和平发展，必须继续坚持“和平统一、`
	fmt.Println(len(a))
	fmt.Println(len([]rune(a)))
	ms := make(map[string][]*Test, 0)
	ms["1"] = append(ms["1"], &Test{H: "aa"})
	fmt.Printf("%++v\n", ms)
	fmt.Println(strconv.ParseInt("030200", 10, 64))
}
