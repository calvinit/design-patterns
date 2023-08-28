import java.util.concurrent.atomic.AtomicLong;

/**
 * 懒汉式（支持延迟加载） - synchronized
 */
public class LazySingleton {
    private final AtomicLong id = new AtomicLong(0);
    /**
     * 因为 synchronized 可以保证其可见性，所以不需要 volatile 关键字
     */
    private static LazySingleton instance;

    /**
     * 私有构造方式，外部不能通过 new 构造新实例
     */
    private LazySingleton() {}

    /**
     * 对外暴露获取单例实例的静态公开方法，synchronized 对整个方法加锁，劣势：并发度很低（为 1，相当于串行操作）
     */
    public static synchronized LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }

    public long getID() {
        return id.incrementAndGet();
    }
}
