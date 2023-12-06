package chain

import (
	"fmt"
	"testing"
)

func TestChainOfResponsibility(t *testing.T) {
	content := "这是一条帖子内容，包含了一些需要过滤的关键词，例如：广告、涉黄、反动 等等。。。"

	filterChain1 := new(SensitiveWordFilterChain1)
	filterChain1.AddFilter(SexyWordFilter1{})
	filterChain1.AddFilter(PoliticalWordFilter1{})
	filterChain1.AddFilter(AdsWordFilter1{})
	legal := filterChain1.Filter(Content{content})
	if legal {
		fmt.Println("1. 恭喜！帖子内容经审查没有问题，可以正常发布！")
	} else {
		fmt.Println("1. WARNING! 帖子内容经审查存在问题，无法正常发布！")
	}

	fmt.Println("==========================================")

	filterChain2 := new(SensitiveWordFilterChain2)
	filterChain2.AddFilter(&SexyWordFilter2{})
	filterChain2.AddFilter(&PoliticalWordFilter2{})
	filterChain2.AddFilter(&AdsWordFilter2{})
	c := &Content{content}
	fmt.Println("2. 帖子内容原文内容为：【", c.Content, "】！")
	filterChain2.Filter(c)
	fmt.Println("2. 经审查过滤后的内容为：【", c.Content, "】，请按此内容发布！")

	fmt.Println("==========================================")

	filterChain3 := NewSensitiveWordFilterChain3(&ConcreateThing{})
	filterChain3.AddFilter(&SexyWordFilter3{})
	filterChain3.AddFilter(&PoliticalWordFilter3{})
	filterChain3.AddFilter(&AdsWordFilter3{})
	filterChain3.Filter(&Content{content})
}

type ConcreateThing struct{}

func (c *ConcreateThing) JustDoIt(content *Content) {
	content.Content = "业务处理中，当前帖子内容被加工，这是修改了后的内容！"
	fmt.Println(">>> 3. 正在做具体的事情，帖子内容为：" + content.Content)
}
