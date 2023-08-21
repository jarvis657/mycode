package main

import (
	"bufio"
	"fmt"
	"strings"
	"sync/atomic"

	"github.com/Jeffail/gabs/v2"
)

var sum int32 = 1

func main() {
	fmt.Println("old sum: ", sum)
	addInt32 := atomic.AddInt32(&sum, 100)
	fmt.Println("addInt32 ", addInt32)
	fmt.Println("new sum: ", sum)
	j := `{"id":"chatcmpl-7449CmDEpJKsJjDOyf6Zj7TJgrDPz","object":"chat.completion","created":1681203610,"model":"gpt-3.5-turbo-0301","usage":{"prompt_tokens":15,"completion_tokens":36,"total_tokens":51},"choices":[{"message":{"role":"assistant","content":"我不知道您想吃什么，您可以自己决定或者提供更多信息，我可以给您建议。"},"finish_reason":"stop","index":0}]}`

	json := GetVFromJson(j, "")
	fmt.Println(json)
	right := strings.TrimRight("cyeamblog.go", ".cggocye")
	fmt.Println(right)

	ss := []string{
		"A",
		"B",
		"C",
	}

	var b strings.Builder
	for _, s := range ss {
		fmt.Fprint(&b, s)
	}

	fmt.Print(b.String(), "\n")
	fmt.Print("==========================\n")
	input := "abcdefghijkl"
	scanner := bufio.NewScanner(strings.NewReader(input))
	//scanner.Split(bufio.ScanWords)
	split := func(data []byte, atEOF bool) (advance int, token []byte, err error) {
		fmt.Printf("%t\t%d\t%s\n", atEOF, len(data), data)
		return 0, nil, nil
	}
	scanner.Split(split)
	buf := make([]byte, 2)
	scanner.Buffer(buf, 1000)
	scan := scanner.Scan()
	fmt.Println(scan)

	//for scanner.Scan() {
	//	fmt.Printf("@@@:%s\n", scanner.Text())
	//}
	jsonTest()
	mm := make(map[string]int)
	mm["a"] = 1
	v, ok := mm["a"]
	fmt.Println(v)
	fmt.Println(ok)
	hh := &Haha{Data: "fdsafds"}
	json2Test(hh)
}

type Haha struct {
	Data string `json:"data"`
}

func jsonTest() {
	parseJSON, _ := gabs.ParseJSON([]byte(`{"type": "openai-101", "email": "openai-101"}`))
	fmt.Println(parseJSON.Path("email").Data())
}
func json2Test(data interface{}) {
	wrap := gabs.Wrap(data)
	encodeJSON := wrap.EncodeJSON(gabs.EncodeOptHTMLEscape(false))
	fmt.Println(string(encodeJSON))
}
func GetVFromJson(jsonStr string, path string) interface{} {
	parseJSON, err := gabs.ParseJSON([]byte(jsonStr))
	if err != nil {
		return ""
	}
	if path == "" {
		return parseJSON.Data()
	}
	return parseJSON.Path(path).Data()
}
