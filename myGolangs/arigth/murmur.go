package main

import (
	"fmt"
	"regexp"
	"strings"
	"time"
)

// Mixing constants; generated offline.
const (
	M = 0x5bd1e995
	R = 24
)

// 32-bit mixing function.
func mmix(h uint32, k uint32) (uint32, uint32) {
	k *= M
	k ^= k >> R
	k *= M
	h *= M
	h ^= k
	return h, k
}

func main() {
	//sampleRegexp := regexp.MustCompile(`(?P\w+):(?P[0-9]\d{1,3})`)
	//
	//input := "The names are John:21, Simon:23, Mike:19"
	//
	//result := sampleRegexp.ReplaceAllString(input, "$Age:$Name")
	//fmt.Println(string(result))
	data := "\n\n\"\\n\\nGreat! I can point you in the \nright direction. Have you checked out Apple's official Developer website? That would be a great resource to get started.\""
	m1 := regexp.MustCompile(`^((\n)*)(\\")?(\\n)*`)
	allString := m1.ReplaceAllString(data, "$3")
	fmt.Println(allString)
	//for ok := strings.Index(data, "\n") >= 0; ok; ok = strings.Index(data, "\n") >= 0 {
	//	data = strings.TrimLeft(data, "\n")
	//}
	replace := strings.Replace(data, "\n", "", -1)
	fmt.Println(replace)
	var location = time.FixedZone("", 9*3600) // 东八
	time.Local = location
	//location, _ := time.LoadLocation("Asia/Shanghai")
	now := time.Now()
	//unix := now.Unix()
	unix := now.In(location).Unix()
	fmt.Println("time:", unix)
	//s := 10
	//i := 1
	//for {
	//	//sprint := fmt.Sprint(s)
	//	//seed := uint32(time.Now().Unix())
	//	//h := MurmurHash2([]byte(sprint), seed)
	//	//a := h % uint32(s)
	//	a := rand.Intn(s)
	//	fmt.Println(i, ":", a)
	//	i++
	//	time.Sleep(time.Second * 1)
	//}
}

// -----------------------------------------------------------------------------

// The original MurmurHash2 32-bit algorithm by Austin Appleby.
func MurmurHash2(data []byte, seed uint32) (h uint32) {
	var k uint32

	// Initialize the hash to a 'random' value
	h = seed ^ uint32(len(data))

	// Mix 4 bytes at a time into the hash
	for l := len(data); l >= 4; l -= 4 {
		k = uint32(data[0]) | uint32(data[1])<<8 | uint32(data[2])<<16 | uint32(data[3])<<24
		h, k = mmix(h, k)
		data = data[4:]
	}

	// Handle the last few bytes of the input array
	switch len(data) {
	case 3:
		h ^= uint32(data[2]) << 16
		fallthrough
	case 2:
		h ^= uint32(data[1]) << 8
		fallthrough
	case 1:
		h ^= uint32(data[0])
		h *= M
	}

	// Do a few final mixes of the hash to ensure the last few bytes are well incorporated
	h ^= h >> 13
	h *= M
	h ^= h >> 15

	return
}
