package main

import (
	"bytes"
	"fmt"
	"strings"
	"testing"
)

func BenchmarkBuffer(b *testing.B) {
	var buf bytes.Buffer
	for i := 0; i < b.N; i++ {
		fmt.Fprint(&buf, "")
		_ = buf.String()
	}
}

func BenchmarkBuilder(b *testing.B) {
	var ch = make(chan int)
	close(ch)
	var builder strings.Builder
	for i := 0; i < b.N; i++ {
		fmt.Fprint(&builder, "")
		_ = builder.String()
	}
}
