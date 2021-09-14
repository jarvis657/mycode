package main

import (
	"crypto/sha256"
	"fmt"
	"sort"
	"strings"
	"sync"

	"git.code.oa.com/trpc-go/trpc-go/errs"
)

type holder struct {
	index int32
}
type Test struct {
	ProcessingTaskIds sync.Map
	name              holder
}

func t1() error {
	return nil
}
func t2() error {
	return errs.New(1, "111")
}

type data struct {
	d string
}

func testDefer(d *data) data {
	s := "a"
	defer func() {
		d.d = s
		fmt.Printf("innter defer d pointer:%p,d.d:%s\n ", &d, d.d)
	}()
	d.d = "b"
	fmt.Printf("innter d pointer:%p,d.d:%s\n ", &d, d.d)
	return *d
}
func test() (i int) {
	defer func() {
		i++
	}()
	return 1
}

func test2() int {
	i := 1
	defer func() {
		i++
	}()
	return i
}
func foo(c chan int) {
	defer close(c)
	a := test()
	if a == 2 {
		c <- 0
		return
	}
	c <- 1
}

func testErr() *error {
	var err *error
	defer func() {
		errr := errs.New(11, "deferErr")
		err = &errr
	}()
	return err
}

type M struct {
	h string
}

func main() {
	err := testErr()
	if err != nil {
		fmt.Printf("err:%v", err)
	}

	ms := make(map[string][]*M)
	ms["1"] = append(ms["1"], &M{"vv"})
	ms["2"] = append(ms["2"], &M{"vv"})
	fmt.Println(ms["3"])
	fmt.Println(ms)

	cc := make(chan int, 1)
	foo(cc)
	fmt.Println(test(), test2())
	sss := "outter"
	{
		sss = "inner"
		fmt.Println(sss)
	}
	fmt.Println(sss)
	//充分说明 return 是在 defer之前执行。因为return是 copy了一个结构，然后defer 修改的是原来的结构
	//innter d pointer:0xc00000e028,d.d:b
	// innter defer d pointer:0xc00000e028,d.d:a
	// d1 pointer:0xc000010250,d1.d:a , s pointer:0xc000010260, s.d:b
	var d1 data
	d1.d = "main"
	s := testDefer(&d1)
	fmt.Printf("d1 pointer:%p,d1.d:%s , s pointer:%p, s.d:%v\n", &d1, d1.d, &s, s.d)

	//var timestamp = Math.round(new Date().getTime() / 1000)
	//var signature = CryptoJS.SHA256(timestamp + token_f +timestamp).toString();
	//timestamp := math.Round(float64(time.Now().Unix()))
	//sum256 := sha256.Sum256([]byte(fmt.Sprintf("%s%s%s", timestamp, "3ebd22c83024131a9d74d6e7bbfa13e7ea0e2bd960dd0bb6f0bf", timestamp)))

	timestamp := 1621846081
	plain := fmt.Sprintf("%d%s%d", timestamp, "3ebd22c83024131a9d74d6e7bbfa13e7ea0e2bd960dd0bb6f0bf", timestamp)
	signature := fmt.Sprintf("%x", sha256.Sum256([]byte(plain)))

	//fmt.Printf("%v,%x",timestamp, sum256[:])
	fmt.Printf("%v,%x", timestamp, signature)

	dd := make([]string, 4)
	dd[0] = "a"
	dd[1] = "b"
	dd[2] = "c"
	dd[3] = "d"
	ddd := dd[:2]
	dd = dd[2:]
	fmt.Printf("ddd:%v====:%v", ddd, len(dd))
	ddd = dd[:2]
	dd = dd[2:]
	fmt.Printf("ddd:%v====:%v", ddd, len(dd))
	//ddd = dd[:2]
	//dd = dd[2:]
	//fmt.Printf("ddd:%v====:%v", ddd, len(dd))

	//for k, v := range mm {
	//	if k == "2" {
	//		delete(mm, "5")
	//		delete(mm, "3")
	//	}
	//	fmt.Printf("k%v,v:%v\n", k, v)
	//}

	var result string
	result = strings.ReplaceAll(result, "\n", "<br />")
	fmt.Printf("%v", result)

	tmpErrorPullStore := make(map[string]int) //是否第一次拉取最后一个pod
	if tmpErrorPullStore["a"] < 10 {
		fmt.Printf("=--==============")
	} else {
		fmt.Printf("=-->>>>>>>>>>>>>>>>>>>>>>.==============")
	}

	key := fmt.Sprintf("%v\001%v", "workload", "cluster")
	ss := strings.Split(key, "\001")
	fmt.Printf("----------->:%v--->%v", ss, "\001")

	//for i := 0; i < 3; i++ {
	//	fmt.Printf("----------->:%v--->%v", i, "\001")
	//	if i == 2 {
	//		return
	//	}
	//}

	cluster_workloads := strings.Split("cls-p83rfhn6:qqcd-test-cjejj,", ",")
	fmt.Println(cluster_workloads)

	m := make([]string, 2)
	m = append(m, "aa")
	m = append(m, "bb")
	fmt.Println(m)

	hs := []holder{{index: 8}, {index: 3}, {index: 1}}
	sort.SliceStable(
		hs, func(i, j int) bool {
			return hs[i].index < hs[j].index
		},
	)
	for i, h := range hs {
		fmt.Printf("%v,%v", i, h)
	}
	fmt.Printf("%v===\n", hs[len(hs)-1])
	tmpPullStore := make(map[string]bool)
	fmt.Printf("%v:", tmpPullStore["aaa"])
	delete(tmpPullStore, "xx")

	split := strings.Split("qqcd-test-cjejj-0", "-")

	fmt.Println(split[len(split)-1])

	ts := make([]int, 2, 2)
	ts = append(ts, 1)
	ts = append(ts, 2)
	fmt.Printf("ts------>%v", len(ts))
	ts = append(ts, 3)
	fmt.Printf("ts----3-->%v", len(ts))

	var err3 error
	err3 = t2()
	err3 = t1()
	fmt.Printf("haha e:%v", err3)

	g := make(map[string]map[int32][]string, 0)
	fmt.Printf("@@@@@@@@@:%v", g["1"][1])
	for i := 0; i < 10; i++ {
		for j := 0; j < 3; j++ {
			if g[fmt.Sprint(i)] == nil {
				g[fmt.Sprint(i)] = make(map[int32][]string)
			}
			if g[fmt.Sprint(i)][int32(j)] == nil {
				g[fmt.Sprint(i)][int32(j)] = make([]string, 0)
			}
			g[fmt.Sprint(i)][int32(j)] = append(g["1"][int32(j)], fmt.Sprintf("%v", j))
		}
	}
	fmt.Printf(">>>>>>>%+v\n", g)

	//test.ProcessingTaskIds.Range(func(key, value interface{}) bool {
	//	if key == "b" {
	//		test.ProcessingTaskIds.Delete("b")
	//		fmt.Printf("delete k:%v,v:%v \n", key, value)
	//	} else {
	//		fmt.Printf("k:%v,v:%v  \n", key, value)
	//	}
	//	return true
	//})
	//test.ProcessingTaskIds.Range(func(key, value interface{}) bool {
	//	fmt.Printf("k:%v,v:%v \n", key, value)
	//	return true
	//})
	fmt.Println("================")
	//if v, ok := test.ProcessingTaskIds.Load("a"); ok {
	//	fmt.Printf("k:%v,v:%v \n", v, ok)
	//} else {
	//	fmt.Printf("no key =========== %v,%v \n", v, ok)
	//}
	data := make(map[string]string)
	data["a"] = "1"
	data["b"] = "2"
	data["c"] = "3"
	//test.t1("vvvvvvv")
	//fmt.Println("t:%s", test.name)
}
