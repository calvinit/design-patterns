package strategy

import "testing"

func TestStrategy(t *testing.T) {
	paySelector := PaySelector{}

	var curr, amount = CNY, float32(500.3)
	paySelector.GetPayStrategy(curr, amount).Pay(curr, amount)

	curr, amount = USD, 1500.67
	paySelector.GetPayStrategy(curr, amount).Pay(curr, amount)

	curr, amount = CNY, 2934.33
	paySelector.GetPayStrategy(curr, amount).Pay(curr, amount)

	curr, amount = USD, 5201
	paySelector.GetPayStrategy(curr, amount).Pay(curr, amount)

	curr, amount = CNY, 10000
	paySelector.GetPayStrategy(curr, amount).Pay(curr, amount)
}
