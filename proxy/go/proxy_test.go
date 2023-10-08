package proxy

import "testing"

func TestStaticProxy(t *testing.T) {
	var biz IBiz = NewStaticProxyInstance()
	biz.JustDoIt()
}
