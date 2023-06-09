package main

import (
	"fmt"

	"github.com/pkoukk/tiktoken-go"
	"github.com/sashabaranov/go-openai"
	"github.com/tiktoken-go/tokenizer"
)

func NumTokensFromMessages(messages []openai.ChatCompletionMessage, model string) (num_tokens int) {
	tkm, err := tiktoken.EncodingForModel(model)
	if err != nil {
		err = fmt.Errorf("EncodingForModel: %v", err)
		fmt.Println(err)
		return
	}

	var tokens_per_message int
	var tokens_per_name int
	if model == "gpt-3.5-turbo-0301" || model == "gpt-3.5-turbo" {
		tokens_per_message = 4
		tokens_per_name = -1
	} else if model == "gpt-4-0314" || model == "gpt-4" {
		tokens_per_message = 3
		tokens_per_name = 1
	} else {
		fmt.Println("Warning: model not found. Using cl100k_base encoding.")
		tokens_per_message = 3
		tokens_per_name = 1
	}

	for _, message := range messages {
		num_tokens += tokens_per_message
		num_tokens += len(tkm.Encode(message.Content, nil, nil))
		num_tokens += len(tkm.Encode(message.Role, nil, nil))
		num_tokens += len(tkm.Encode(message.Name, nil, nil))
		if message.Name != "" {
			num_tokens += tokens_per_name
		}
	}
	num_tokens += 3
	return num_tokens
}

func text_print() {
	text := "你好"
	encoding := "gpt-3.5-turbo"

	tke, err := tiktoken.EncodingForModel(encoding)
	if err != nil {
		err = fmt.Errorf("getEncoding: %v", err)
		return
	}

	// encode
	token := tke.Encode(text, nil, nil)

	//tokens
	fmt.Println((token))
	// num_tokens
	fmt.Println(len(token))
}
func text_print2() {
	//enc, err := tokenizer.Get(tokenizer.GPT35Turbo)
	enc, err := tokenizer.ForModel(tokenizer.GPT4)
	if err != nil {
		panic("oh oh")
	}

	// this should print a list of token ids
	ids, _, _ := enc.Encode("你好")
	fmt.Println(ids)

	// this should print the original string back
	text, _ := enc.Decode(ids)
	fmt.Println(text)
}

func invokeNum() {
	msgs := []openai.ChatCompletionMessage{{
		Role:    "user",
		Content: "你好",
	}}
	tokens := NumTokensFromMessages(msgs, "gpt-3.5-turbo")
	fmt.Println(tokens)
}

func main() {
	text_print()
	fmt.Println("=================")
	text_print2()
	fmt.Println("=================")
	invokeNum()
}
