package observer

import (
	"fmt"
	"slices"
)

type Subject interface {
	RegisterObserver(observer SyncBlockingObserver)
	RemoveObserver(observer SyncBlockingObserver)
	NotifyObservers(message string)
}

type SyncBlockingObserver interface {
	Update(message string)
}

// ============================================================

type ConcreteSubject struct {
	observers []SyncBlockingObserver
}

func (s *ConcreteSubject) RegisterObserver(observer SyncBlockingObserver) {
	s.observers = append(s.observers, observer)
}

func (s *ConcreteSubject) RemoveObserver(observer SyncBlockingObserver) {
	s.observers = slices.DeleteFunc(s.observers, func(o SyncBlockingObserver) bool {
		return o == observer
	})
}

func (s *ConcreteSubject) NotifyObservers(message string) {
	for _, o := range s.observers {
		o.Update(message)
	}
}

type ConcreteObserverOne struct{}

func (ConcreteObserverOne) Update(message string) {
	fmt.Println("ConcreteObserverOne is notified, message:", message)
}

type ConcreteObserverTwo struct{}

func (ConcreteObserverTwo) Update(message string) {
	fmt.Println("ConcreteObserverTwo is notified, message:", message)
}
