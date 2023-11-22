import java.util.List;
import java.util.concurrent.TimeUnit;

public class ObserverTest {
    public static void main(String[] args) throws InterruptedException {
        Subject subject = new ConcreteSubject();
        subject.registerObserver(new ConcreteObserverOne());
        subject.registerObserver(new ConcreteObserverTwo());
        subject.notifyObservers("hello world");

        System.out.println("=====================================================");

        UserController userController = new UserController(new UserService());
        RegPromotionObserver promotion = new RegPromotionObserver();
        RegNotificationObserver notification = new RegNotificationObserver();
        userController.setRegObservers(List.of(promotion, notification));
        Long userId = userController.register("13800138000", "123!@#abc");
        System.out.printf("1. [%s] ObserverTest.main: %d 注册成功\n", Thread.currentThread(), userId);
        userController.unregObservers(List.of(notification));
        userId = userController.register("13800138001", "321!@#ABC");
        System.out.printf("2. [%s] ObserverTest.main: %d 注册成功\n", Thread.currentThread(), userId);
        TimeUnit.MILLISECONDS.sleep(300L);
    }
}
