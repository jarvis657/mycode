package main

import "fmt"

type notifer interface {
	notify()
}

type user struct {
	name  string
	email string
}

type admin struct {
	name string
	age  int
}

type ad struct {
	name string
	age  int
}

//使用指针接收者实现了notofy接口,方法会共享接收者所指向的值user
func (u *user) notify() {
	fmt.Println("sendNotify to user", u.name)
}

//使用值接收者实现了notofy接口,方法使用u值的副本,对u的修改不会影响原值
func (u admin) notify() {
	fmt.Println("sendNotify to admin:", u.name)
}

//接收一个notifer接口类型的值，如果一个实体类型实现了该接口，
//sendNotify函数会根据实体类型的值类执行notifer接口的notify行为，这个函数具有多态的能力。
func sendNotify(n notifer) {
	n.notify()
}

func main_() {
	user1 := user{"张三", "qq@qq.com"}
	sendNotify(&user1)

	user2 := admin{"李四", 25}
	sendNotify(user2)

	var n notifer
	n = &user1
	n.notify()
}
