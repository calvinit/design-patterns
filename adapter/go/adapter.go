package adapter

import "fmt"

type ITarget interface {
	f1()
	f2()
	fc()
}

// ============================================================

type Adaptee struct{}

func (a Adaptee) fa() {
	fmt.Println("I'm Adaptee.fa()")
}

func (a Adaptee) fb() {
	fmt.Println("I'm Adaptee.fb()")
}

func (a Adaptee) fc() {
	fmt.Println("I'm Adaptee.fc()")
}

// ============================================================

type Adapter struct {
	Adaptee
}

func (a Adapter) f1() {
	fmt.Println("I'm Adapter.f1(), I adapted Adaptee.fa().")
	a.fa()
}

func (a Adapter) f2() {
	fmt.Println("I'm Adapter.f2(), I adapted Adaptee.fb().")
	a.fb()
}

func (a Adapter) fc() {
	fmt.Println("I'm Adapter.fc(), I adapted Adaptee.fc().")
	a.Adaptee.fc()
}
