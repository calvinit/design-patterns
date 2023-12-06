package chain

import (
	"fmt"
	"strings"
)

type Content struct {
	Content string
}

// ============================================================

type SensitiveWordFilter1 interface {
	doFilter(content Content) bool
}

type SexyWordFilter1 struct{}

func (f SexyWordFilter1) doFilter(content Content) bool {
	fmt.Println("1. 「情色内容」过滤中。。。")
	legal := !strings.Contains(content.Content, "涉黄")
	legal = legal && !strings.Contains(content.Content, "儿童不宜")
	return legal
}

type PoliticalWordFilter1 struct{}

func (f PoliticalWordFilter1) doFilter(content Content) bool {
	fmt.Println("1. 「政治敏感内容」过滤中。。。")
	legal := !strings.Contains(content.Content, "反动")
	legal = legal && !strings.Contains(content.Content, "不符合社会价值观")
	return legal
}

type AdsWordFilter1 struct{}

func (f AdsWordFilter1) doFilter(content Content) bool {
	fmt.Println("1. 「广告内容」过滤中。。。")
	return !strings.Contains(content.Content, "广告")
}

type SensitiveWordFilterChain1 struct {
	filters []SensitiveWordFilter1
}

func (c *SensitiveWordFilterChain1) AddFilter(filter1 SensitiveWordFilter1) {
	c.filters = append(c.filters, filter1)
}

func (c *SensitiveWordFilterChain1) Filter(content Content) bool {
	for _, filter := range c.filters {
		if !filter.doFilter(content) {
			return false
		}
	}
	return true
}

// ============================================================

type SensitiveWordFilter2 interface {
	doFilter(content *Content)
}

type Successor struct {
	doFilter  func(content *Content)
	successor *Successor // next 后缀过滤器
}

func (f *Successor) SetSuccessor(successor *Successor) {
	f.successor = successor
}

func (f *Successor) Filter(content *Content) {
	f.doFilter(content)
	if f.successor != nil {
		f.successor.Filter(content)
	}
}

type SexyWordFilter2 struct{}

func (f *SexyWordFilter2) doFilter(content *Content) {
	fmt.Println("2. 「情色内容」过滤中。。。")
	c := strings.ReplaceAll(content.Content, "涉黄", "**")
	content.Content = strings.Replace(c, "儿童不宜", "****", -1) // the same as `strings.ReplaceAll(c, "儿童不宜", "****")`
}

type PoliticalWordFilter2 struct{}

func (f *PoliticalWordFilter2) doFilter(content *Content) {
	fmt.Println("2. 「政治敏感内容」过滤中。。。")
	c := strings.ReplaceAll(content.Content, "反动", "##")
	content.Content = strings.ReplaceAll(c, "不符合社会价值观", "########")
}

type AdsWordFilter2 struct{}

func (f *AdsWordFilter2) doFilter(content *Content) {
	fmt.Println("2. 「广告内容」过滤中。。。")
	content.Content = strings.ReplaceAll(content.Content, "广告", "$$")
}

type SensitiveWordFilterChain2 struct {
	head *Successor
	tail *Successor
}

func (c *SensitiveWordFilterChain2) AddFilter(filter2 SensitiveWordFilter2) {
	successor := &Successor{filter2.doFilter, nil}
	if c.head == nil {
		c.head = successor
		c.tail = successor
		return
	}
	c.tail.SetSuccessor(successor)
	c.tail = successor
}

func (c *SensitiveWordFilterChain2) Filter(content *Content) {
	if c.head != nil {
		c.head.Filter(content)
	}
}

// ============================================================

type SensitiveWordFilter3 interface {
	doFilter(content *Content, chain *SensitiveWordFilterChain3)
}

type SexyWordFilter3 struct{}

func (f *SexyWordFilter3) doFilter(content *Content, chain *SensitiveWordFilterChain3) {
	fmt.Println("3.1 「情色内容」过滤中。。。")
	c := strings.ReplaceAll(content.Content, "涉黄", "**")
	content.Content = strings.ReplaceAll(c, "儿童不宜", "****")
	fmt.Println("3.1 拦截请求，请求前的帖子内容为：", content.Content)
	chain.Filter(content)
	fmt.Println("3.1 拦截响应，响应前的帖子内容为：", content.Content)
}

type PoliticalWordFilter3 struct{}

func (f *PoliticalWordFilter3) doFilter(content *Content, chain *SensitiveWordFilterChain3) {
	fmt.Println("3.2 「政治敏感内容」过滤中。。。")
	c := strings.ReplaceAll(content.Content, "反动", "##")
	content.Content = strings.ReplaceAll(c, "不符合社会价值观", "########")
	fmt.Println("3.2 拦截请求，请求前的帖子内容为：", content.Content)
	chain.Filter(content)
	fmt.Println("3.2 拦截响应，响应前的帖子内容为：", content.Content)
}

type AdsWordFilter3 struct{}

func (f *AdsWordFilter3) doFilter(content *Content, chain *SensitiveWordFilterChain3) {
	fmt.Println("3.3 「广告内容」过滤中。。。")
	content.Content = strings.ReplaceAll(content.Content, "广告", "$$")
	fmt.Println("3.3 拦截请求，请求前的帖子内容为：", content.Content)
	chain.Filter(content)
	fmt.Println("3.3 拦截响应，响应前的帖子内容为：", content.Content)
}

type Something interface {
	JustDoIt(content *Content)
}

type SensitiveWordFilterChain3 struct {
	filters []SensitiveWordFilter3

	pos int // 当前执行到了哪个 filter
	n   int // filter 的个数

	something Something
}

func NewSensitiveWordFilterChain3(something Something) *SensitiveWordFilterChain3 {
	return &SensitiveWordFilterChain3{something: something}
}

func (c *SensitiveWordFilterChain3) AddFilter(filter3 SensitiveWordFilter3) {
	for _, f := range c.filters {
		if filter3 == f {
			return
		}
	}
	c.filters = append(c.filters, filter3)
	c.n++
}

func (c *SensitiveWordFilterChain3) Filter(content *Content) {
	if c.pos < c.n {
		filter3 := c.filters[c.pos]
		c.pos++
		filter3.doFilter(content, c)
	} else {
		c.something.JustDoIt(content)
	}
}
