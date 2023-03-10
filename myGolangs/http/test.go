package main

import (
	"bytes"
	"crypto/tls"
	"encoding/json"
	"fmt"
	"net/http"
	"net/url"
	"time"
)

type BotConfig struct {
	BotToken string `json:"botToken"`
	ChatId   string `json:"chatId"`
}

func test() {
	fmt.Println("--------")
}

func a() {
	defer test()
	return
}

func main() {
	a()
	//funcName()
}

func funcName() {
	bot := BotConfig{}
	bot.ChatId = "-843624557"
	bot.BotToken = "6128082530:AAHoyoMxmUEZGgUlQLRi3AoBTJMHNxu473w"

	pUrl := "https://api.telegram.org/bot" + bot.BotToken + "/sendMessage"
	params := map[string]interface{}{}
	params["chat_id"] = bot.ChatId
	params["text"] = "test✔️⚠️❌ "
	data, err := json.Marshal(params)
	if err != nil {
		fmt.Println("参数json化失败:" + err.Error())
		return
	}
	param := bytes.NewBuffer(data)
	var client http.Client
	//if *proxyAddr != "" {
	//proxy, err := url.Parse(*proxyAddr)
	if err != nil {
		fmt.Println("proxy 解析失败:" + err.Error())
		return
	}
	proxyUrl, err := url.Parse("socks5://127.0.0.1:7890")
	client = http.Client{
		Timeout: time.Second * 10,
		Transport: &http.Transport{
			TLSClientConfig: &tls.Config{InsecureSkipVerify: false},
			Proxy:           http.ProxyURL(proxyUrl),
		},
	}
	//} else {
	//	client = http.Client{
	//		Timeout: 5 * time.Second, // 5秒最大超时
	//	}
	//}
	resp, err := client.Post(pUrl, "application/json", param)
	if err != nil {
		fmt.Println("发起请求失败:" + err.Error())
		return
	}
	defer resp.Body.Close()
}
