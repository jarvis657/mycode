package main

import "fmt"

func main() {
	//a := 0
	//fmt.Scan(&a)
	//fmt.Printf("%d\n", a)
	fmt.Printf("Hello World!\n")
	// [1,0,1] [2,1,2] [3,1,3] [4,2,4] [5,2,5]
	var nodeSlice []Node = []Node{
		{4, 2, 4},
		{5, 2, 5},
		{1, 0, 1},
		{2, 1, 2},
		{3, 1, 3},
	}
	var ret *NodeList = merge(nodeSlice)
	fmt.Printf("%#v", ret)
	show(ret)
}
func show(nodeList *NodeList) {

}

type NodeList struct {
	NextList []*NodeList
	Val      int
	Id       int
}
type Node struct {
	//原结构
	Id     int
	Parent int
	Val    int
}

func merge(originSlice []Node) *NodeList {
	var nodeMap map[int]*NodeList = make(map[int]*NodeList, len(originSlice))
	for _, nodePtr := range originSlice {
		var tmpNode *NodeList = &NodeList{Id: nodePtr.Id, Val: nodePtr.Val, NextList: []*NodeList{}}
		nodeMap[nodePtr.Id] = tmpNode
	}
	nodeMap[0] = &NodeList{}
	var ret *NodeList
	for _, nodePtr := range originSlice {
		nodeMap[nodePtr.Parent].NextList = append(nodeMap[nodePtr.Parent].NextList, nodeMap[nodePtr.Id])
		if nodePtr.Parent == 0 {
			ret = nodeMap[nodePtr.Id]
		}
	}
	return ret
}
