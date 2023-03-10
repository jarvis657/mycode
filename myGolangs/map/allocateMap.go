package main

import (
	"log"
	"runtime"
)

var intMap map[int]int
var cnt = 8192

func main() {
	for i := 0; i < 10000; i++ {
		main_run()
	}
}

func main_run() {
	printMemStats()

	initMap()
	runtime.GC()
	printMemStats()

	log.Println(len(intMap))
	for i := 0; i < cnt; i++ {
		delete(intMap, i)
	}
	log.Println(len(intMap))

	runtime.GC()
	printMemStats()

	intMap = nil
	runtime.GC()
	printMemStats()
}

func initMap() {
	intMap = make(map[int]int, cnt)

	for i := 0; i < cnt; i++ {
		intMap[i] = i
	}
}

func printMemStats() {
	var m runtime.MemStats
	runtime.ReadMemStats(&m)
	log.Printf("HeadInUse=%v Alloc = %v TotalAlloc = %v Sys = %v NumGC = %v\n", m.HeapInuse/1024, m.Alloc/1024, m.TotalAlloc/1024, m.Sys/1024, m.NumGC)
}
