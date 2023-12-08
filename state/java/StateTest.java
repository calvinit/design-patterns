public class StateTest {
    public static void main(String[] args) {
        MarioStateMachine1 mario1 = new MarioStateMachine1();
        mario1.eatMushRoom();
        System.out.printf("mario1 score: %d, state: %s\n", mario1.getScore(), mario1.getCurrentState());

        MarioStateMachine2 mario2 = new MarioStateMachine2();
        mario2.eatMushRoom();
        System.out.printf("mario2 score: %d, state: %s\n", mario2.getScore(), mario2.getCurrentState());

        MarioStateMachine3 mario3 = new MarioStateMachine3();
        mario3.eatMushRoom();
        System.out.printf("mario3 score: %d, state: %s\n", mario3.getScore(), mario3.getCurrentState());
    }
}
