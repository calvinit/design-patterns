package proxy

import "fmt"

type IBiz interface {
	JustDoIt()
}

type BizImpl struct{}

func NewBizImpl() *BizImpl {
	return &BizImpl{}
}

func (b *BizImpl) JustDoIt() {
	fmt.Println("==> Do it in BizImpl.")
}

type StaticProxy struct {
	biz *BizImpl
}

func NewStaticProxyInstance() *StaticProxy {
	return &StaticProxy{NewBizImpl()}
}

func (p *StaticProxy) JustDoIt() {
	if p.biz == nil {
		p.biz = NewBizImpl()
	}
	fmt.Println("1. Static-proxy: before biz.JustDoIt().")
	p.biz.JustDoIt()
	fmt.Println("1. Static-proxy: after biz.JustDoIt().")
}
