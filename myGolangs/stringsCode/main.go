package main

import (
	"bufio"
	"fmt"
	"strings"
	"sync/atomic"
)

var sum int32 = 1

func main() {
	fmt.Println("old sum: ", sum)
	addInt32 := atomic.AddInt32(&sum, 100)
	fmt.Println("addInt32 ", addInt32)
	fmt.Println("new sum: ", sum)

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
}
