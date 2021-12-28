package utils

import "time"

func CurrentTimestampToInt() int32 {
	return int32(time.Now().Unix())
}
