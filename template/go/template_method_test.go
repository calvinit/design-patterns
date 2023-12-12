package template

import (
	"fmt"
	"testing"
)

func TestTemplateMethod(t *testing.T) {
	recipient := NewRecipient("张三", "13800138000", "zhangsan@email.com", "地球村")
	message := "这是一条有用的消息，请查阅！"

	s := NewSMSSender()
	s.Send(recipient, message)

	fmt.Println("==========================================")

	s = NewEmailSender()
	s.Send(recipient, message)

	fmt.Println("==========================================")

	s = NewLetterSender()
	s.Send(recipient, message)
}
