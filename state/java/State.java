public enum State {
    SMALL(0), // 小马里奥
    SUPER(1), // 超级马里奥
    FIRE(2),  // 火焰马里奥
    CAPE(3);  // 斗篷马里奥

    private final int value;

    State(int value) {
        this.value = value;
    }

    public int stateValue() {
        return value;
    }
}

// ============================================================

// 「马里奥」状态机 - 分支逻辑法（实现简单的状态机）
class MarioStateMachine1 {
    private int score;
    private State currentState;

    public MarioStateMachine1() {
        score = 0;
        currentState = State.SMALL;
    }

    // 吃了蘑菇 🍄
    public void eatMushRoom() {
        if (currentState == State.SMALL) {
            score += 100;
            currentState = State.SUPER;
        }
    }

    // 获得斗篷
    public void obtainCape() {
        if (currentState == State.SMALL || currentState == State.SUPER) {
            score += 200;
            currentState = State.CAPE;
        }
    }

    // 获得火焰 🔥
    public void obtainFireFlower() {
        if (currentState == State.SMALL || currentState == State.SUPER) {
            score += 300;
            currentState = State.FIRE;
        }
    }

    // 遇到怪物
    public void meetMonster() {
        if (currentState == State.SUPER) {
            score -= 100;
            currentState = State.SMALL;
        } else if (currentState == State.CAPE) {
            score -= 200;
            currentState = State.SMALL;
        } else if (currentState == State.FIRE) {
            score -= 300;
            currentState = State.SMALL;
        }
    }

    public int getScore() {
        return score;
    }

    public State getCurrentState() {
        return currentState;
    }
}

// ============================================================

enum Event {
    ATE_MUSH_ROOM(0),
    GOT_CAPE(1),
    GOT_FIRE(2),
    MET_MONSTER(3);

    private final int value;

    Event(int value) {
        this.value = value;
    }

    public int eventValue() {
        return value;
    }
}

// 「马里奥」状态机 - 查表法（实现相对比较复杂的状态机 - 状态很多、状态转移比较复杂）
class MarioStateMachine2 {
    private int score;
    private State currentState;

    private static final int[][] ACTION_TABLE = {
            {+100, +200, +300, +0},
            {+0, +200, +300, -100},
            {+0, +0, +0, -200},
            {+0, +0, +0, -300}
    };
    private static final State[][] TRANSITION_TABLE = {
            {State.SUPER, State.CAPE, State.FIRE, State.SMALL},
            {State.SUPER, State.CAPE, State.FIRE, State.SMALL},
            {State.CAPE, State.CAPE, State.CAPE, State.SMALL},
            {State.FIRE, State.FIRE, State.FIRE, State.SMALL}
    };

    public MarioStateMachine2() {
        score = 0;
        currentState = State.SMALL;
    }

    private void executeEvent(Event event) {
        int stateValue = currentState.stateValue();
        int eventValue = event.eventValue();
        this.score += ACTION_TABLE[stateValue][eventValue];
        this.currentState = TRANSITION_TABLE[stateValue][eventValue];
    }

    // 吃了蘑菇 🍄
    public void eatMushRoom() {
        executeEvent(Event.ATE_MUSH_ROOM);
    }

    // 获得斗篷
    public void obtainCape() {
        executeEvent(Event.GOT_CAPE);
    }

    // 获得火焰 🔥
    public void obtainFireFlower() {
        executeEvent(Event.GOT_FIRE);
    }

    // 遇到怪物
    public void meetMonster() {
        executeEvent(Event.MET_MONSTER);
    }

    public int getScore() {
        return score;
    }

    public State getCurrentState() {
        return currentState;
    }
}

// ============================================================

interface IMario {
    State state();

    // 吃了蘑菇 🍄
    default void eatMushRoom(MarioStateMachine3 mario3) {}

    // 获得斗篷
    default void obtainCape(MarioStateMachine3 mario3) {}

    // 获得火焰 🔥
    default void obtainFireFlower(MarioStateMachine3 mario3) {}

    // 遇到怪物
    default void meetMonster(MarioStateMachine3 mario3) {}
}

class SmallMario implements IMario {
    private static final SmallMario MARIO = new SmallMario();

    private SmallMario() {}

    public static SmallMario getInstance() {
        return MARIO;
    }

    @Override
    public State state() {
        return State.SMALL;
    }

    @Override
    public void eatMushRoom(MarioStateMachine3 mario3) {
        mario3.addScore(100);
        mario3.setCurrentState(SuperMario.getInstance());
    }

    @Override
    public void obtainCape(MarioStateMachine3 mario3) {
        mario3.addScore(200);
        mario3.setCurrentState(CapeMario.getInstance());
    }

    @Override
    public void obtainFireFlower(MarioStateMachine3 mario3) {
        mario3.addScore(300);
        mario3.setCurrentState(FireMario.getInstance());
    }
}

class SuperMario implements IMario {
    private static final SuperMario MARIO = new SuperMario();

    private SuperMario() {}

    public static SuperMario getInstance() {
        return MARIO;
    }

    @Override
    public State state() {
        return State.SUPER;
    }

    @Override
    public void obtainCape(MarioStateMachine3 mario3) {
        mario3.addScore(200);
        mario3.setCurrentState(CapeMario.getInstance());
    }

    @Override
    public void obtainFireFlower(MarioStateMachine3 mario3) {
        mario3.addScore(300);
        mario3.setCurrentState(FireMario.getInstance());
    }

    @Override
    public void meetMonster(MarioStateMachine3 mario3) {
        mario3.addScore(-100);
        mario3.setCurrentState(SmallMario.getInstance());
    }
}

class CapeMario implements IMario {
    private static final CapeMario MARIO = new CapeMario();

    private CapeMario() {}

    public static CapeMario getInstance() {
        return MARIO;
    }

    @Override
    public State state() {
        return State.CAPE;
    }

    @Override
    public void meetMonster(MarioStateMachine3 mario3) {
        mario3.addScore(-200);
        mario3.setCurrentState(SmallMario.getInstance());
    }
}

class FireMario implements IMario {
    private static final FireMario MARIO = new FireMario();

    private FireMario() {}

    public static FireMario getInstance() {
        return MARIO;
    }

    @Override
    public State state() {
        return State.FIRE;
    }

    @Override
    public void meetMonster(MarioStateMachine3 mario3) {
        mario3.addScore(-300);
        mario3.setCurrentState(SmallMario.getInstance());
    }
}

// 「马里奥」状态机 - 状态模式（实现相对比较复杂的状态机 - 状态并不多、状态转移也比较简单，但事件触发执行的动作包含的业务逻辑可能比较复杂）
class MarioStateMachine3 {
    private int score;
    private IMario currentState;

    public MarioStateMachine3() {
        score = 0;
        currentState = SmallMario.getInstance();
    }

    // 吃了蘑菇 🍄
    public void eatMushRoom() {
        currentState.eatMushRoom(this);
    }

    // 获得斗篷
    public void obtainCape() {
        currentState.obtainCape(this);
    }

    // 获得火焰 🔥
    public void obtainFireFlower() {
        currentState.obtainFireFlower(this);
    }

    // 遇到怪物
    public void meetMonster() {
        currentState.meetMonster(this);
    }

    public int getScore() {
        return score;
    }

    public void addScore(int delta) {
        this.score += delta;
    }

    public State getCurrentState() {
        return currentState.state();
    }

    public void setCurrentState(IMario mario) {
        this.currentState = mario;
    }
}