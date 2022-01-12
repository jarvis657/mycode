package main

import (
	"fmt"
	"sync"
)

type iOtp interface {
	genRandomOTP(int) string
	saveOTPCache(string)
	getMessage(string) string
	sendNotification(string) error
	publishMetric()
}
type otp struct {
	iOtp
}

func (o *otp) genAndSendOTP(otpLength int) error {
	otp := o.genRandomOTP(otpLength)
	o.saveOTPCache(otp)
	message := o.getMessage(otp)
	err := o.sendNotification(message)
	if err != nil {
		return err
	}
	o.publishMetric()
	return nil
}

type sms struct {
	otp
}

func (s *sms) genRandomOTP(len int) string {
	randomOTP := "1234"
	fmt.Printf("SMS: generating random otp %s\n", randomOTP)
	return randomOTP
}

func (s *sms) saveOTPCache(otp string) {
	fmt.Printf("SMS: saving otp: %s to cache\n", otp)
}

func (s *sms) getMessage(otp string) string {
	return "SMS OTP for login is " + otp
}

func (s *sms) sendNotification(message string) error {
	fmt.Printf("SMS: sending sms: %s\n", message)
	return nil
}

func (s *sms) publishMetric() {
	fmt.Printf("SMS: publishing metrics\n")
}

type email struct {
	otp
}

func (s *email) genRandomOTP(len int) string {
	randomOTP := "1234"
	fmt.Printf("EMAIL: generating random otp %s\n", randomOTP)
	return randomOTP
}

func (s *email) saveOTPCache(otp string) {
	fmt.Printf("EMAIL: saving otp: %s to cache\n", otp)
}

func (s *email) getMessage(otp string) string {
	return "EMAIL OTP for login is " + otp
}

func (s *email) sendNotification(message string) error {
	fmt.Printf("EMAIL: sending email: %s\n", message)
	return nil
}

func (s *email) publishMetric() {
	fmt.Printf("EMAIL: publishing metrics\n")
}

func main() {
	var wg sync.WaitGroup
	wg.Wait()
	var a int
	var ptr *int
	var pptr **int

	a = 3000

	/* take the address of var */
	ptr = &a

	/* take the address of ptr using address of operator & */
	pptr = &ptr
	fmt.Printf("Value of a = %d\n", a)
	fmt.Printf("Value available at *ptr = %d\n", ptr)
	fmt.Printf(
		"Value available at **pptr = %d\n", pptr,
	)
	/* take the value using pptr */
	fmt.Printf("Value of a = %d\n", a)
	fmt.Printf("Value available at *ptr = %d\n", *ptr)
	fmt.Printf(
		"Value available at **pptr = %d\n", **pptr,
	)
	smsOTP := &sms{}
	o := otp{
		iOtp: smsOTP,
	}
	o.genAndSendOTP(4)

	fmt.Println("")
	emailOTP := &email{}
	o = otp{
		iOtp: emailOTP,
	}
	o.genAndSendOTP(4)
}
