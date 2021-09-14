package main

import "fmt"

type fileError struct {
}

func (fe *fileError) Error() string {
	return "文件错误"
}
func main_ww() {
	conent, err := openFile()
	if err != nil {
		fmt.Println(err)
	} else {
		fmt.Println("=======", string(conent))
	}
}

//返回*fileError这样不行，因为隐式转换会导致是nil 不是nil的问题
//func openFile() ([]byte, *fileError) {
//	//return nil, &fileError{}
//	return nil, nil
//}
//只是模拟一个错误
func openFile() ([]byte, error) {
	return nil, &fileError{}
}
