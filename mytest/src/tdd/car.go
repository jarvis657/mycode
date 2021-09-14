package tdd

//方向
var E = [2]int32{1, 0}
var N = [2]int32{0, 1}
var W = [2]int32{-1, 0}
var S = [2]int32{0, -1}

var events = make([][2]int32, 0)

var current_Turn [2]int32

type Car struct {
	x int32
	y int32
}

//receiveEvent 接受指令
func receiveEvent(t []string) [][2]int32 {
	return events
}

//initCar 初始化car
func initCar(car Car, x int32, y int32) *Car {
	return nil
}

// moveCar 移动
func moveCar(car Car, move [2]int32) *Car {
	return nil
}

//当前信息
func info(car Car) *[2]int32 {
	return nil
}

// 转向
func turn(t string) *[2]int32 {
	switch t {
	case "E":
		return &E
	case "N":
		return &N
	case "W":
		return &W
	case "S":
		return &S
	}
	return nil
}
