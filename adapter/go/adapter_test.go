package adapter

import "testing"

func TestAdapter(t *testing.T) {
	var target ITarget = Adapter{Adaptee{}}
	target.f1()
	target.f2()
	target.fc()
}
