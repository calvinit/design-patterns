package observer

import (
	"fmt"
	"testing"
	"time"
)

func TestObserver(t *testing.T) {
	var subject Subject = new(ConcreteSubject)
	subject.RegisterObserver(new(ConcreteObserverOne))
	subject.RegisterObserver(new(ConcreteObserverTwo))
	subject.NotifyObservers("hello world")

	fmt.Println("=====================================================")

	userController := NewUserController()
	promotion := RegPromotionObserver{}.HandleRegSuccess
	notification := RegNotificationObserver{}.HandleRegSuccess
	userController.SetRegObservers([]any{promotion, notification})
	userID := userController.Register("13800138000", "123!@#abc")
	fmt.Printf("1. observer_test.TestObserver: %d 注册成功\n", userID)
	userController.UnregObservers([]any{notification})
	userID = userController.Register("13800138001", "321!@#ABC")
	fmt.Printf("2. observer_test.TestObserver: %d 注册成功\n", userID)
	time.Sleep(300 * time.Millisecond)
}
