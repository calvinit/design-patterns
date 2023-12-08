package state

import (
	"fmt"
	"testing"
)

func TestState(t *testing.T) {
	mario1 := NewMarioStateMachine1()
	mario1.EatMushRoom()
	fmt.Printf("mario1 score: %d, state: %s\n", mario1.GetScore(), mario1.GetCurrentState())

	mario2 := NewMarioStateMachine2()
	mario2.EatMushRoom()
	fmt.Printf("mario2 score: %d, state: %s\n", mario2.GetScore(), mario2.GetCurrentState())

	mario3 := NewMarioStateMachine3()
	mario3.EatMushRoom()
	fmt.Printf("mario3 score: %d, state: %s\n", mario3.GetScore(), mario3.GetCurrentState())
}
