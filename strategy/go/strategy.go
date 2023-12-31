package strategy

import "fmt"

type Currency uint8

const (
	USD Currency = iota
	CNY Currency = iota
)

var currNames = [...]string{"USD", "CNY"}

func (curr Currency) Value() uint8 {
	return uint8(curr)
}

func (curr Currency) Name() string {
	return currNames[curr]
}

func (curr Currency) String() string {
	return curr.Name()
}

// ============================================================

// PayStrategy 支付策略
type PayStrategy interface {
	shouldBeChosen(curr Currency, amount float32) bool
	Pay(curr Currency, amount float32)
}

type payByDebitCard struct{}

func (p payByDebitCard) shouldBeChosen(_ Currency, amount float32) bool {
	return amount <= 1000
}

func (p payByDebitCard) Pay(curr Currency, amount float32) {
	fmt.Printf("【DebitCard】支付款项 %s %.2f。\n", curr, amount)
}

type payByPayPal struct{}

func (p payByPayPal) shouldBeChosen(curr Currency, amount float32) bool {
	return curr == USD && amount > 1000 && amount < 3000
}

func (p payByPayPal) Pay(curr Currency, amount float32) {
	fmt.Printf("【PayPal】支付款项 %s %.2f。\n", curr, amount)
}

type payByAliPay struct{}

func (p payByAliPay) shouldBeChosen(curr Currency, amount float32) bool {
	return curr == CNY && amount > 1000 && amount < 3000
}

func (p payByAliPay) Pay(curr Currency, amount float32) {
	fmt.Printf("【AliPay】支付款项 %s %.2f。\n", curr, amount)
}

type payByCreditCard struct{}

func (p payByCreditCard) shouldBeChosen(_ Currency, amount float32) bool {
	return amount >= 3000
}

func (p payByCreditCard) Pay(curr Currency, amount float32) {
	fmt.Printf("【CreditCard】支付款项 %s %.2f。\n", curr, amount)
}

type payByCash struct{}

func (p payByCash) shouldBeChosen(_ Currency, _ float32) bool {
	return false
}

func (p payByCash) Pay(curr Currency, amount float32) {
	fmt.Printf("【Cash】支付款项 %s %.2f。\n", curr, amount)
}

// ============================================================

var payStrategyList = []PayStrategy{
	&payByDebitCard{},
	&payByPayPal{},
	&payByAliPay{},
	&payByCreditCard{},
	&payByCash{},
}

type PaySelector struct{}

func (PaySelector) GetPayStrategy(curr Currency, amount float32) PayStrategy {
	for _, payStrategy := range payStrategyList {
		if payStrategy.shouldBeChosen(curr, amount) {
			return payStrategy
		}
	}
	return payStrategyList[len(payStrategyList)-1]
}
