import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

// 参考 Google Guava EventBus 通用框架实现简易的事件总线
class EventBus {
    private final Executor executor;
    private final ObserverRegistry registry = new ObserverRegistry();

    public EventBus() {
        this(null);
    }

    protected EventBus(Executor executor) {
        this.executor = executor;
    }

    public void register(Object observer) {
        registry.register(observer);
    }

    public void unregister(Object observer) {
        registry.unregister(observer);
    }

    public void post(Object event) {
        List<ObserverAction> observerActions = registry.getMatchedObserverActions(event);
        if (executor == null) {
            observerActions.forEach(o -> o.execute(event));
        } else {
            observerActions.forEach(o -> executor.execute(() -> o.execute(event)));
        }
    }
}

class AsyncEventBus extends EventBus {
    public AsyncEventBus(Executor executor) {
        super(executor);
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Subscribe {}

/**
 * @param target 观察者类
 */
record ObserverAction(Object target, Method method) {
    ObserverAction(Object target, Method method) {
        assert target != null;
        this.target = target;
        this.method = method;
        this.method.setAccessible(true);
    }

    /**
     * 执行 method
     *
     * @param event method 方法的参数
     */
    public void execute(Object event) {
        try {
            method.invoke(target, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

class ObserverRegistry {
    private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<ObserverAction>> registry = new ConcurrentHashMap<>();

    public void register(Object observer) {
        Map<Class<?>, Collection<ObserverAction>> observerActions = findAllObserverActions(observer);
        for (Map.Entry<Class<?>, Collection<ObserverAction>> entry : observerActions.entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<ObserverAction> eventActions = entry.getValue();
            registry.computeIfAbsent(eventType, key -> new CopyOnWriteArraySet<>()).addAll(eventActions);
        }
    }

    public void unregister(Object observer) {
        Map<Class<?>, Collection<ObserverAction>> observerActions = findAllObserverActions(observer);
        Iterator<Map.Entry<Class<?>, Collection<ObserverAction>>> iterator = observerActions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class<?>, Collection<ObserverAction>> entry = iterator.next();
            CopyOnWriteArraySet<ObserverAction> actions = registry.get(entry.getKey());
            if (actions != null && !actions.isEmpty()) {
                actions.removeIf(eventAction -> eventAction.target().getClass() == observer.getClass());
                if (actions.isEmpty()) {
                    iterator.remove();
                }
            }
        }
    }

    public List<ObserverAction> getMatchedObserverActions(Object event) {
        List<ObserverAction> matchedObservers = new ArrayList<>();
        Class<?> postedEventType = event.getClass();
        for (Map.Entry<Class<?>, CopyOnWriteArraySet<ObserverAction>> entry : registry.entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<ObserverAction> eventActions = entry.getValue();
            if (postedEventType.isAssignableFrom(eventType)) {
                matchedObservers.addAll(eventActions);
            }
        }
        return matchedObservers;
    }

    private Map<Class<?>, Collection<ObserverAction>> findAllObserverActions(Object observer) {
        Map<Class<?>, Collection<ObserverAction>> observerActions = new HashMap<>();
        Class<?> clazz = observer.getClass();
        for (Method method : getAnnotatedMethods(clazz)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> eventType = parameterTypes[0];
            observerActions.computeIfAbsent(eventType, key -> new ArrayList<>()).add(new ObserverAction(observer, method));
        }
        return observerActions;
    }

    private List<Method> getAnnotatedMethods(Class<?> clazz) {
        List<Method> annotatedMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException(String.format("Method %s has @Subscribe annotation but has %s parameters. Subscriber methods must have exactly 1 parameter.", method, parameterTypes.length));
                }
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }
}

// ============================================================

class UserController {
    private final UserService userService;

    private final EventBus eventBus;

    public UserController(UserService userService) {
        this.userService = userService;
        // this.eventBus = new EventBus();                                                 // 同步阻塞模式
        this.eventBus = new AsyncEventBus(Executors.newVirtualThreadPerTaskExecutor()); // 异步非阻塞模式
    }

    public void setRegObservers(List<Object> observers) {
        for (Object observer : observers) {
            eventBus.register(observer);
        }
    }

    public void unregObservers(List<Object> observers) {
        for (Object observer : observers) {
            eventBus.unregister(observer);
        }
    }

    public Long register(String telephone, String password) {
        // 省略输入参数的校验代码...
        long userId = userService.register(telephone, password);
        eventBus.post(userId);
        return userId;
    }
}

class UserService {
    public Long register(String telephone, String password) {
        System.out.printf("[%s] UserService.register：telephone(%s), password(%s)\n", Thread.currentThread(), telephone, password);
        return Math.abs(ThreadLocalRandom.current().nextLong());
    }
}

class RegPromotionObserver {
    @Subscribe
    public void handleRegSuccess(Long userId) {
        System.out.printf("[%s] RegPromotionObserver.handleRegSuccess: %d 注册成功\n", Thread.currentThread(), userId);
    }
}

class RegNotificationObserver {
    @Subscribe
    public void handleRegSuccess(Long userId) {
        System.out.printf("[%s] RegNotificationObserver.handleRegSuccess: %d 注册成功\n", Thread.currentThread(), userId);
    }
}