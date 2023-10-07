package main

import (
	"fmt"
	"runtime"
	"unsafe"
)

const N = 129

func randBytes() [N]byte {
	return [N]byte{}
}

func printAlloc() {
	var m runtime.MemStats
	runtime.ReadMemStats(&m)
	fmt.Printf("%d MB\n", m.Alloc/1024/1024)
}

func main() {
	n := 1_000_000
	m := make(map[int][N]byte)
	printAlloc()

	var B uint8
	for i := 0; i < n; i++ {
		curB := *(*uint8)(
			unsafe.Pointer(
				uintptr(
					unsafe.Pointer(
						*(**int)(unsafe.Pointer(&m)),
					),
				) + 9),
		)
		if B != curB {
			fmt.Println(curB)
			B = curB
		}
		m[i] = randBytes()
	}
	printAlloc()

	for i := 0; i < n; i++ {
		delete(m, i)
	}

	runtime.GC()
	printAlloc()
	//如果不加最后的 KeepAlive，m 会被回收掉。
	runtime.KeepAlive(m)
}
