package main

import (
	"container/heap"
	"fmt"
	"math"
)

// 用于存储节点和边的结构体
type Node struct {
	id   int
	dist float64
}

type Edge struct {
	from   int
	to     int
	weight float64
}

// 定义图结构
type Graph struct {
	nodes []Node
	edges []Edge
	graph map[int][]Edge
}

// 初始化图
func (g *Graph) Init() {
	g.graph = make(map[int][]Edge)
	for _, node := range g.nodes {
		g.graph[node.id] = []Edge{}
	}
	for _, edge := range g.edges {
		g.graph[edge.from] = append(g.graph[edge.from], edge)
	}
}

// 实现堆排序接口
type PriorityQueue []Node

func (pq PriorityQueue) Len() int { return len(pq) }

func (pq PriorityQueue) Less(i, j int) bool {
	return pq[i].dist < pq[j].dist
}

func (pq PriorityQueue) Swap(i, j int) {
	pq[i], pq[j] = pq[j], pq[i]
}

func (pq *PriorityQueue) Push(x interface{}) {
	item := x.(Node)
	*pq = append(*pq, item)
}

func (pq *PriorityQueue) Pop() interface{} {
	old := *pq
	n := len(old)
	item := old[n-1]
	*pq = old[0 : n-1]
	return item
}

// Dijkstra 算法实现
func Dijkstra(graph Graph, start int, end int) []int {
	dist := make(map[int]float64)
	prev := make(map[int]int)
	visited := make(map[int]bool)
	pq := &PriorityQueue{}
	heap.Init(pq)

	// 初始化距离和前驱节点
	for _, node := range graph.nodes {
		dist[node.id] = math.Inf(1)
		prev[node.id] = -1
	}

	// 将起点加入队列
	heap.Push(pq, Node{id: start, dist: 0})
	dist[start] = 0

	// 运行 Dijkstra 算法
	for pq.Len() > 0 {
		// 取出距离起点最近的节点
		node := heap.Pop(pq).(Node)
		visited[node.id] = true

		// 更新相邻节点的距离和前驱节点
		for _, edge := range graph.graph[node.id] {
			if visited[edge.to] {
				continue
			}
			newDist := dist[node.id] + edge.weight
			if newDist < dist[edge.to] {
				dist[edge.to] = newDist
				prev[edge.to] = node.id
				heap.Push(pq, Node{id: edge.to, dist: newDist})
			}
		}
	}

	// 构造最短路径
	path := []int{}
	node := end
	for node != -1 {
		path = append([]int{node}, path...)
		node = prev[node]
	}

	// 返回最短路径和距离
	return path
}

func main() {
	graph := Graph{
		nodes: []Node{
			{id: 1},
			{id: 2},
			{id: 3},
			{id: 4},
			{id: 5},
		},
		edges: []Edge{
			{from: 1, to: 2, weight: 10},
			{from: 1, to: 3, weight: 5},
			{from: 2, to: 3, weight: 2},
			{from: 2, to: 4, weight: 1},
			{from: 3, to: 2, weight: 3},
			{from: 3, to: 4, weight: 9},
			{from: 3, to: 5, weight: 2},
			{from: 4, to: 5, weight: 4},
			{from: 5, to: 4, weight: 6},
		},
	}
	graph.Init()

	path := Dijkstra(graph, 1, 5)
	fmt.Printf("最短路径: %v", path)
}
