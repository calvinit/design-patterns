package state

import "sync"

type State int

const (
	SMALL State = iota // 小马里奥
	SUPER              // 超级马里奥
	FIRE               // 火焰马里奥
	CAPE               // 斗篷马里奥
)

var stateNames = [...]string{"SMALL", "SUPER", "FIRE", "CAPE"}

func (state State) Value() int {
	return int(state)
}

func (state State) Name() string {
	return stateNames[state]
}

func (state State) String() string {
	return state.Name()
}

// ============================================================

// MarioStateMachine1 「马里奥」状态机 - 分支逻辑法（实现简单的状态机）
type MarioStateMachine1 struct {
	score        int
	currentState State
}

func NewMarioStateMachine1() *MarioStateMachine1 {
	return &MarioStateMachine1{
		score:        0,
		currentState: SMALL,
	}
}

// EatMushRoom 吃了蘑菇 🍄
func (m *MarioStateMachine1) EatMushRoom() {
	if m.currentState == SMALL {
		m.score += 100
		m.currentState = SUPER
	}
}

// ObtainCape 获得斗篷
func (m *MarioStateMachine1) ObtainCape() {
	if m.currentState == SMALL || m.currentState == SUPER {
		m.score += 200
		m.currentState = CAPE
	}
}

// ObtainFireFlower 获得火焰 🔥
func (m *MarioStateMachine1) ObtainFireFlower() {
	if m.currentState == SMALL || m.currentState == SUPER {
		m.score += 300
		m.currentState = FIRE
	}
}

// MeetMonster 遇到怪物
func (m *MarioStateMachine1) MeetMonster() {
	if m.currentState == SUPER {
		m.score -= 100
		m.currentState = SMALL
	} else if m.currentState == CAPE {
		m.score -= 200
		m.currentState = SMALL
	} else if m.currentState == FIRE {
		m.score -= 300
		m.currentState = SMALL
	}
}

func (m *MarioStateMachine1) GetScore() int {
	return m.score
}

func (m *MarioStateMachine1) GetCurrentState() State {
	return m.currentState
}

// ============================================================

type Event int

const (
	ATE_MUSH_ROOM Event = iota
	GOT_CAPE
	GOT_FIRE
	MET_MONSTER
)

var eventNames = [...]string{"ATE_MUSH_ROOM", "GOT_CAPE", "GOT_FIRE", "MET_MONSTER"}

func (event Event) Value() int {
	return int(event)
}

func (event Event) Name() string {
	return eventNames[event]
}

func (event Event) String() string {
	return event.Name()
}

var actionTable = [4][4]int{
	{+100, +200, +300, +0},
	{+0, +200, +300, -100},
	{+0, +0, +0, -200},
	{+0, +0, +0, -300},
}

var transitionTable = [4][4]State{
	{SUPER, CAPE, FIRE, SMALL},
	{SUPER, CAPE, FIRE, SMALL},
	{CAPE, CAPE, CAPE, SMALL},
	{FIRE, FIRE, FIRE, SMALL},
}

// MarioStateMachine2 「马里奥」状态机 - 查表法（实现相对比较复杂的状态机 - 状态很多、状态转移比较复杂）
type MarioStateMachine2 struct {
	score        int
	currentState State
}

func NewMarioStateMachine2() *MarioStateMachine2 {
	return &MarioStateMachine2{
		score:        0,
		currentState: SMALL,
	}
}

func (m *MarioStateMachine2) executeEvent(event Event) {
	stateValue := m.currentState.Value()
	eventValue := event.Value()
	m.score += actionTable[stateValue][eventValue]
	m.currentState += transitionTable[stateValue][eventValue]
}

// EatMushRoom 吃了蘑菇 🍄
func (m *MarioStateMachine2) EatMushRoom() {
	m.executeEvent(ATE_MUSH_ROOM)
}

// ObtainCape 获得斗篷
func (m *MarioStateMachine2) ObtainCape() {
	m.executeEvent(GOT_CAPE)
}

// ObtainFireFlower 获得火焰 🔥
func (m *MarioStateMachine2) ObtainFireFlower() {
	m.executeEvent(GOT_FIRE)
}

// MeetMonster 遇到怪物
func (m *MarioStateMachine2) MeetMonster() {
	m.executeEvent(MET_MONSTER)
}

func (m *MarioStateMachine2) GetScore() int {
	return m.score
}

func (m *MarioStateMachine2) GetCurrentState() State {
	return m.currentState
}

// ============================================================

type IMario interface {
	State() State

	EatMushRoom(mario3 *MarioStateMachine3)      // 吃了蘑菇 🍄
	ObtainCape(mario3 *MarioStateMachine3)       // 获得斗篷
	ObtainFireFlower(mario3 *MarioStateMachine3) // 获得火焰 🔥
	MeetMonster(mario3 *MarioStateMachine3)      // 遇到怪物
}

type Mario struct{}

func (Mario) EatMushRoom(*MarioStateMachine3) {}

func (Mario) ObtainCape(*MarioStateMachine3) {}

func (Mario) ObtainFireFlower(*MarioStateMachine3) {}

func (Mario) MeetMonster(*MarioStateMachine3) {}

type SmallMario struct {
	Mario
}

var (
	smallMarioOnce sync.Once
	smallMario     *SmallMario
)

func SmallMarioSingleton() *SmallMario {
	smallMarioOnce.Do(func() {
		smallMario = &SmallMario{Mario{}}
	})
	return smallMario
}

func (*SmallMario) State() State {
	return SMALL
}

func (*SmallMario) EatMushRoom(mario3 *MarioStateMachine3) {
	mario3.AddScore(100)
	mario3.SetCurrentState(SuperMarioSingleton())
}

func (*SmallMario) ObtainCape(mario3 *MarioStateMachine3) {
	mario3.AddScore(200)
	mario3.SetCurrentState(CapeMarioSingleton())
}

func (*SmallMario) ObtainFireFlower(mario3 *MarioStateMachine3) {
	mario3.AddScore(300)
	mario3.SetCurrentState(FireMarioSingleton())
}

type SuperMario struct {
	Mario
}

var (
	superMarioOnce sync.Once
	superMario     *SuperMario
)

func SuperMarioSingleton() *SuperMario {
	superMarioOnce.Do(func() {
		superMario = &SuperMario{Mario{}}
	})
	return superMario
}

func (*SuperMario) State() State {
	return SUPER
}

func (*SuperMario) ObtainCape(mario3 *MarioStateMachine3) {
	mario3.AddScore(200)
	mario3.SetCurrentState(CapeMarioSingleton())
}

func (*SuperMario) ObtainFireFlower(mario3 *MarioStateMachine3) {
	mario3.AddScore(300)
	mario3.SetCurrentState(FireMarioSingleton())
}

func (*SuperMario) MeetMonster(mario3 *MarioStateMachine3) {
	mario3.AddScore(-100)
	mario3.SetCurrentState(SmallMarioSingleton())
}

type CapeMario struct {
	Mario
}

var (
	capeMarioOnce sync.Once
	capeMario     *CapeMario
)

func CapeMarioSingleton() *CapeMario {
	capeMarioOnce.Do(func() {
		capeMario = &CapeMario{Mario{}}
	})
	return capeMario
}

func (m *CapeMario) State() State {
	return CAPE
}

func (m *CapeMario) MeetMonster(mario3 *MarioStateMachine3) {
	mario3.AddScore(-200)
	mario3.SetCurrentState(SmallMarioSingleton())
}

type FireMario struct {
	Mario
}

var (
	fireMarioOnce sync.Once
	fireMario     *FireMario
)

func FireMarioSingleton() *FireMario {
	fireMarioOnce.Do(func() {
		fireMario = &FireMario{Mario{}}
	})
	return fireMario
}

func (m *FireMario) State() State {
	return FIRE
}

func (m *FireMario) MeetMonster(mario3 *MarioStateMachine3) {
	mario3.AddScore(-300)
	mario3.SetCurrentState(SmallMarioSingleton())
}

// MarioStateMachine3 「马里奥」状态机 - 状态模式（实现相对比较复杂的状态机 - 状态并不多、状态转移也比较简单，但事件触发执行的动作包含的业务逻辑可能比较复杂）
type MarioStateMachine3 struct {
	score        int
	currentState IMario
}

func NewMarioStateMachine3() *MarioStateMachine3 {
	return &MarioStateMachine3{
		score:        0,
		currentState: SmallMarioSingleton(),
	}
}

// EatMushRoom 吃了蘑菇 🍄
func (m *MarioStateMachine3) EatMushRoom() {
	m.currentState.EatMushRoom(m)
}

// ObtainCape 获得斗篷
func (m *MarioStateMachine3) ObtainCape() {
	m.currentState.ObtainCape(m)
}

// ObtainFireFlower 获得火焰 🔥
func (m *MarioStateMachine3) ObtainFireFlower() {
	m.currentState.ObtainFireFlower(m)
}

// MeetMonster 遇到怪物
func (m *MarioStateMachine3) MeetMonster() {
	m.currentState.MeetMonster(m)
}

func (m *MarioStateMachine3) GetScore() int {
	return m.score
}

func (m *MarioStateMachine3) AddScore(delta int) {
	m.score += delta
}

func (m *MarioStateMachine3) GetCurrentState() State {
	return m.currentState.State()
}

func (m *MarioStateMachine3) SetCurrentState(mario IMario) {
	m.currentState = mario
}
