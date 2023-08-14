package main

import (
	"errors"
	"fmt"
)

func main() {
	defT()
}

type Config struct{}
type Body struct{}

func (b *Body) Close() {

}

func (c *Config) Close() {
	fmt.Printf("Closing.................")
	var b *Body
	b = nil
	b.Close()
}

func defT() {
	steam, err := SendChatRequestSteam()
	defer steam.Close()
	if err != nil {
		return
	}
}
func SendChatRequestSteam() (*Config, error) {
	return nil, errors.New("aaa")
}
