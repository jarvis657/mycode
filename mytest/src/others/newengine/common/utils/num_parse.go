package utils

func Interface2Int32(i interface{}) int32 {
	num, _ := i.(int32)
	return num
}
