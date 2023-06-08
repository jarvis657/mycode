package main

import (
	"encoding/json"
	"fmt"
)

// APIError provides error information returned by the OpenAI API.
type APIError struct {
	Code       *string `json:"code,omitempty"`
	Message    string  `json:"message"`
	Param      *string `json:"param,omitempty"`
	Type       string  `json:"type"`
	StatusCode int     `json:"-"`
}

// RequestError provides informations about generic request errors.
type RequestError struct {
	StatusCode int
	Err        error
}

type ErrorResponse struct {
	Err *APIError `json:"error,omitempty"`
}

func (e *ErrorResponse) Error() string {
	return fmt.Sprintf("error, status code: %d, message: %s", e.Err.Code, e.Err.Message)
}

func (e *APIError) Error() string {
	return e.Message
}

func (e *RequestError) Error() string {
	if e.Err != nil {
		return e.Err.Error()
	}
	return fmt.Sprintf("status code %d", e.StatusCode)
}

func (e *RequestError) Unwrap() error {
	return e.Err
}

func main() {
	j := `{"error": {"message": "You exceeded your current quota, please check your plan and billing details.","type": "insufficient_quota","param": null,"code": null}}`
	var e *ErrorResponse
	err := json.Unmarshal([]byte(j), e)
	fmt.Printf("%v\n", err)
}
