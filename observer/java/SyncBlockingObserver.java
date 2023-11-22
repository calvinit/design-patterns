import java.util.ArrayList;
import java.util.List;

// 被观察者（Observable）和观察者（Observer）的多种叫法：
// 如：Subject-Observer、Publisher-Subscriber、Producer-Consumer、EventEmitter-EventListener、Dispatcher-Listener 等
interface Subject {
    void registerObserver(SyncBlockingObserver observer);

    void removeObserver(SyncBlockingObserver observer);

    void notifyObservers(String message);
}

// 同步阻塞式的观察者
public interface SyncBlockingObserver {
    void update(String message);
}

// ============================================================

class ConcreteSubject implements Subject {
    private final List<SyncBlockingObserver> observers = new ArrayList<>();

    @Override
    public void registerObserver(SyncBlockingObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(SyncBlockingObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        // 同步阻塞调用每个观察者的 update 方法以通知它去做后续逻辑变更或业务处理
        observers.forEach(o -> o.update(message));
    }
}

// ============================================================

class ConcreteObserverOne implements SyncBlockingObserver {
    @Override
    public void update(String message) {
        System.out.println("ConcreteObserverOne is notified, message: " + message);
    }
}

class ConcreteObserverTwo implements SyncBlockingObserver {
    @Override
    public void update(String message) {
        System.out.println("ConcreteObserverTwo is notified, message: " + message);
    }
}