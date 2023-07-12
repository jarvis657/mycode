package main

import (
	"fmt"
	"log"

	"code.sajari.com/docconv"
	"github.com/gabriel-vasile/mimetype"
)

func main() {
	var ccc any
	ccc = "aa"

	fmt.Println(ccc == 1)
	//buf, _ := ioutil.ReadFile("sample.jpg")
	////file, _ := os.Open("/Users/jarvis/Downloads/chatgpt设计.pdf")
	//file, _ := os.Open("/Users/jarvis/Downloads/wenshu_detail.sql")
	////file, _ := os.Open("/Users/jarvis/Downloads/chatgpt设计.docx")
	//// We only have to pass the file header = first 261 bytes
	//head := make([]byte, 3072)
	//file.Read(head)
	//if filetype.IsImage(head) {
	//	fmt.Println("File is an image")
	//} else {
	//	fmt.Println("Not an image")
	//}
	//kind, _ := filetype.Match(head)
	//if kind == filetype.Unknown {
	//	fmt.Println("Unknown file type")
	//}
	//fmt.Printf("==>File type: %s. MIME: %s\n", kind.Extension, kind.MIME.Value)
	file, err2 := mimetype.DetectFile("/Users/jarvis/Downloads/chatgpt设计.pdf")
	fmt.Println(err2)
	fmt.Println(file)
	res, err := docconv.ConvertPath("/Users/jarvis/Downloads/chatgpt设计.pdf")
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println(res.Body)
}
