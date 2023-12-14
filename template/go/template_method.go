package template

import (
	"log"
	"regexp"
	"strings"
	"unicode/utf8"
)

type Recipient struct {
	name    string
	phone   string
	email   string
	address string
}

func NewRecipient(name, phone, email, address string) *Recipient {
	return &Recipient{
		name:    name,
		phone:   phone,
		email:   email,
		address: address,
	}
}

type ISender interface {
	Send(recipient *Recipient, message string) bool
}

type validator interface {
	recipientIsValid(recipient *Recipient) bool
	messageIsValid(message string) bool
}

type sender struct {
	validator
	sender ISender
}

func (s *sender) Send(recipient *Recipient, message string) bool {
	if !s.recipientIsValid(recipient) {
		log.Fatalln("ERROR! 收件人信息不合法，无法发送消息！")
		return false
	}
	if !s.messageIsValid(message) {
		log.Fatalln("ERROR! 消息内容不合法，无法发送消息！")
		return false
	}
	if s.sender.Send(recipient, message) {
		log.Println("恭喜！消息发送成功！")
		return true
	} else {
		log.Panicln("WARNING! 消息发送失败！")
		return false
	}
}

type SMSSender struct{}

func NewSMSSender() ISender {
	s := SMSSender{}
	return &sender{
		validator: s,
		sender:    s,
	}
}

func (SMSSender) recipientIsValid(recipient *Recipient) bool {
	isMatch, err := regexp.MatchString("^1[3456789]\\d{9}$", recipient.phone)
	if err != nil {
		return false
	}
	return isMatch
}

func (SMSSender) messageIsValid(message string) bool {
	return utf8.RuneCountInString(message) <= 500
}

func (SMSSender) Send(recipient *Recipient, message string) bool {
	log.Printf("向「%s」的手机号码 [%s] 发送短信完成，短信内容为：【%s】！\n", recipient.name, recipient.phone, message)
	return true
}

type EmailSender struct{}

func NewEmailSender() ISender {
	s := EmailSender{}
	return &sender{
		validator: s,
		sender:    s,
	}
}

func (EmailSender) recipientIsValid(recipient *Recipient) bool {
	isMatch, err := regexp.MatchString(
		"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
		recipient.email)
	if err != nil {
		return false
	}
	return isMatch
}

func (EmailSender) messageIsValid(message string) bool {
	return len(message) <= 4*1024*1024
}

func (EmailSender) Send(recipient *Recipient, message string) bool {
	log.Printf("向「%s」的电子邮箱 [%s] 发送邮件完成，邮件内容为：【%s】！\n", recipient.name, recipient.email, message)
	return true
}

type LetterSender struct{}

func NewLetterSender() ISender {
	s := LetterSender{}
	return &sender{
		validator: s,
		sender:    s,
	}
}

func (LetterSender) recipientIsValid(recipient *Recipient) bool {
	return len(strings.TrimSpace(recipient.address)) > 0
}

func (LetterSender) messageIsValid(message string) bool {
	return len(strings.TrimSpace(message)) > 0
}

func (LetterSender) Send(recipient *Recipient, message string) bool {
	log.Printf("向「%s」的收件地址 [%s] 邮递信件完成，信件内容为：【%s】！\n", recipient.name, recipient.address, message)
	return true
}
