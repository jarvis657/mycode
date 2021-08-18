package email

import (
	"fmt"
	"log"

	//"net/smtp"
	"strings"
	"time"
)

type Mail struct {
	Subject string
	Data    []byte
	Target  []string
}

var (
	Host       string
	MailServer string
	MailUser   string
	MailPasswd string
	MailTarget string
	mailChan   chan Mail
)

func init() {
	initialMailAgent()
	Host = "smtp.mogujie.com"
	MailServer = "smtp.mogujie.com:25"
	MailUser = "qishan@mogujie.com"
	//TODO
	MailPasswd = "llllllllllllllll"
	//MailTarget = "qishan@mogujie.com,jufeng@mogujie.com,yideng@mogujie.com"
	MailTarget = "qishan@mogujie.com"
}

func initialMailAgent() {
	mailChan = make(chan Mail, 1024)
	go func() {
		for mail := range mailChan {
			SendMailMain(mail.Subject, mail.Data, mail.Target...)
		}
	}()

}
func SendMailMain(subject string, msg []byte, target ...string) {
	var targetAddr []string
	if len(target) == 0 {
		targetAddr = strings.Split(MailTarget, ",")
	} else {
		targetAddr = target
	}
	fromAddr := MailUser
	displayToAddr := strings.Join(targetAddr, ",")
	mm := fmt.Sprintf("From: %s\r\nTo: %s\r\nSubject: %s\r\nContent-Type: text/plain; charset=UTF-8\r\n\r\n", fromAddr, displayToAddr, subject)
	message := mm + string(msg) + "\r\n" + time.Now().String()
	auth := LoginAuth(MailUser, MailPasswd, Host)
	err := SendMail(Host, MailServer, auth, fromAddr, targetAddr, []byte(message))
	if err != nil {
		log.Printf("Send mail fail: [%s], err=[%s] \n", message, err)
	}
}

//MSendMail 发邮件
func MSendMail(subject string, msg []byte, target ...string) {
	mailChan <- Mail{subject, msg, target}
}
