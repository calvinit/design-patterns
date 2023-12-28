package mediator

import "fmt"

// Train 列车接口（组件）
type Train interface {
	Arrive()
	Depart()
	PermitArrival()
}

type concreteTrain struct {
	mediator Mediator
}

// PassengerTrain 旅客列车（具体组件）
type PassengerTrain concreteTrain

func NewPassengerTrain(mediator Mediator) *PassengerTrain {
	return &PassengerTrain{mediator}
}

func (p *PassengerTrain) Arrive() {
	if !p.mediator.canArrive(p) {
		fmt.Println("PassengerTrain: Arrival blocked, waiting")
		return
	}
	fmt.Println("PassengerTrain: Arrived")
}

func (p *PassengerTrain) Depart() {
	fmt.Println("PassengerTrain: Leaving")
	p.mediator.notifyAboutDeparture()
}

func (p *PassengerTrain) PermitArrival() {
	fmt.Println("PassengerTrain: Arrival permitted, arriving")
	p.Arrive()
}

// FreightTrain 货运列车（具体组件）
type FreightTrain concreteTrain

func NewFreightTrain(mediator Mediator) *FreightTrain {
	return &FreightTrain{mediator}
}

func (f *FreightTrain) Arrive() {
	if !f.mediator.canArrive(f) {
		fmt.Println("FreightTrain: Arrival blocked, waiting")
		return
	}
	fmt.Println("FreightTrain: Arrived")
}

func (f *FreightTrain) Depart() {
	fmt.Println("FreightTrain: Leaving")
	f.mediator.notifyAboutDeparture()
}

func (f *FreightTrain) PermitArrival() {
	fmt.Println("FreightTrain: Arrival permitted, arriving")
	f.Arrive()
}

// Mediator 中介者接口
type Mediator interface {
	canArrive(Train) bool
	notifyAboutDeparture()
}

// StationManager 车站经理（具体中介者）
type StationManager struct {
	isPlatformFree bool
	trainQueue     []Train
}

func NewStationManger() *StationManager {
	return &StationManager{
		isPlatformFree: true,
	}
}

func (s *StationManager) canArrive(t Train) bool {
	if s.isPlatformFree {
		s.isPlatformFree = false
		return true
	}
	s.trainQueue = append(s.trainQueue, t)
	return false
}

func (s *StationManager) notifyAboutDeparture() {
	if !s.isPlatformFree {
		s.isPlatformFree = true
	}
	if len(s.trainQueue) > 0 {
		firstTrainInQueue := s.trainQueue[0]
		s.trainQueue = s.trainQueue[1:]
		firstTrainInQueue.PermitArrival()
	}
}
