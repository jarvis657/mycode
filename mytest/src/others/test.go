package main

import (
	"log"
)

type User struct {
	Name  string
	Email string
}

type Admin struct {
	User
	Level string
}

func (u *User) Notify() error {
	log.Printf("User: Sending User Email To %s<%s>\n",
		u.Name,
		u.Email)

	return nil
}

type Notifier interface {
	Notify() error
}

func SendNotification(notify Notifier) error {
	return notify.Notify()
}

func _main_() {
	admin := &Admin{
		User: User{
			Name:  "AriesDevil",
			Email: "ariesdevil@xxoo.com",
		},
		Level: "super",
	}

	SendNotification(admin)
}
