package mediator

import "testing"

func TestMediator(t *testing.T) {
	mediator := NewStationManger()

	passengerTrain := NewPassengerTrain(mediator)
	freightTrain := NewFreightTrain(mediator)

	passengerTrain.Arrive()
	freightTrain.Arrive()
	passengerTrain.Depart()
}
