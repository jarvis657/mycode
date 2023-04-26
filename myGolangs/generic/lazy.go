package main

import (
	"fmt"
	"strings"
)

// 一个适用于线性结构的迭代器接口
type Iter[T any] interface{ Next() (T, bool) }

// 用于将任意 slice 包装成 Iter[T]
type SliceIter[T any] struct {
	i int
	s []T
}

func (i *SliceIter[T]) Next() (v T, ok bool) {
	if ok = i.i < len(i.s); ok {
		v = i.s[i.i]
		i.i++
	}
	return
}

func IterOfSlice[T any](s []T) Iter[T] {
	return &SliceIter[T]{s: s}
}

type filterIter[T any] struct {
	f   func(T) bool
	src Iter[T]
}

func (i *filterIter[T]) Next() (v T, ok bool) {
	for {
		v, ok = i.src.Next()
		if !ok || i.f(v) {
			return
		}
	}
}

func LazyFilter[T any](f func(T) bool, src Iter[T]) Iter[T] {
	return &filterIter[T]{f: f, src: src}
}

func List[T any](src Iter[T]) (dst []T) {
	for {
		v, ok := src.Next()
		if !ok {
			return
		}
		dst = append(dst, v)
	}
}

func main() {
	i := IterOfSlice([]int{-2, -1, -0, 1, 2})
	i = LazyFilter(func(v int) bool { return v > -2 }, i)
	i = LazyFilter(func(v int) bool { return v < 2 }, i)
	i = LazyFilter(func(v int) bool { return v != 0 }, i)
	fmt.Println(List(i))

	trim := strings.Trim("javascript中", " ")
	fmt.Println(trim)
}
