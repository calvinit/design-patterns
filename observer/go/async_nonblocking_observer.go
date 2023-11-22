package observer

import (
	"fmt"
	"math/rand"
	"reflect"
	"slices"
	"sync"
	"time"
)

type eventBus interface {
	Register(topic string, observer any) error
	Unregister(topic string, observer any) error
	Post(topic string, events ...any)
}

type EventBus struct {
	registry registry
}

func NewEventBus() *EventBus {
	return &EventBus{registry{&sync.Map{}}}
}

func (b *EventBus) Register(topic string, observer any) error {
	v := reflect.ValueOf(observer)
	if v.Type().Kind() != reflect.Func {
		return fmt.Errorf("observer is not a func")
	}
	b.registry.register(topic, v)
	return nil
}

func (b *EventBus) Unregister(topic string, observer any) error {
	v := reflect.ValueOf(observer)
	if v.Type().Kind() != reflect.Func {
		return fmt.Errorf("observer is not a func")
	}
	b.registry.unregister(topic, v)
	return nil
}

func (b *EventBus) Post(topic string, events ...any) {
	if observers := b.registry.getObservers(topic); observers != nil {
		params := make([]reflect.Value, len(events))
		for i, event := range events {
			params[i] = reflect.ValueOf(event)
		}
		for _, observer := range observers {
			observer.Call(params)
		}
	}
}

type AsyncEventBus struct {
	EventBus
}

func NewAsyncEventBus() *AsyncEventBus {
	return &AsyncEventBus{EventBus{registry{&sync.Map{}}}}
}

func (b *AsyncEventBus) Post(topic string, events ...any) {
	if observers := b.registry.getObservers(topic); observers != nil {
		params := make([]reflect.Value, len(events))
		for i, event := range events {
			params[i] = reflect.ValueOf(event)
		}
		for _, observer := range observers {
			go observer.Call(params)
		}
	}
}

type registry struct {
	observers *sync.Map
}

func (r registry) register(topic string, observerFunc reflect.Value) {
	v, _ := r.observers.LoadOrStore(topic, []reflect.Value{})
	sl := append(v.([]reflect.Value), observerFunc)
	r.observers.Store(topic, sl)
}

func (r registry) unregister(topic string, observerFunc reflect.Value) {
	if v, ok := r.observers.Load(topic); ok {
		sl := slices.DeleteFunc(v.([]reflect.Value), func(o reflect.Value) bool {
			return o == observerFunc
		})
		r.observers.Store(topic, sl)
	}
}

func (r registry) getObservers(topic string) []reflect.Value {
	if v, ok := r.observers.Load(topic); ok {
		return v.([]reflect.Value)
	}
	return nil
}

// ============================================================

type UserController struct {
	userService UserService
	eventBus    eventBus
	topic       string
}

func NewUserController() *UserController {
	return &UserController{
		userService: UserService{},
		// eventBus: NewEventBus(),
		eventBus: NewAsyncEventBus(),
		topic:    "reg-success",
	}
}

func (c *UserController) SetRegObservers(observers []any) {
	for _, observer := range observers {
		_ = c.eventBus.Register(c.topic, observer)
	}
}

func (c *UserController) UnregObservers(observers []any) {
	for _, observer := range observers {
		_ = c.eventBus.Unregister(c.topic, observer)
	}
}

func (c *UserController) Register(telephone, password string) int64 {
	userID := c.userService.Register(telephone, password)
	c.eventBus.Post(c.topic, userID)
	return userID
}

type UserService struct{}

func (u UserService) Register(telephone, password string) int64 {
	fmt.Printf("UserService.register：telephone(%s), password(%s)\n", telephone, password)
	s := rand.NewSource(time.Now().UnixNano())
	r := rand.New(s)
	for {
		if v := r.Int63(); v != 0 {
			if v < 0 {
				return -v
			}
			return v
		}
	}
}

type RegPromotionObserver struct{}

func (o RegPromotionObserver) HandleRegSuccess(userID int64) {
	fmt.Printf("RegPromotionObserver.HandleRegSuccess: %d 注册成功\n", userID)
}

type RegNotificationObserver struct{}

func (o RegNotificationObserver) HandleRegSuccess(userID int64) {
	fmt.Printf("RegNotificationObserver.HandleRegSuccess: %d 注册成功\n", userID)
}
