import java.util.concurrent.atomic.AtomicLong;

/**
 * 静态内部类（支持延迟加载）
 */
public class StaticInnerClassSingleton {
    private final AtomicLong id = new AtomicLong(0);

    /**
     * 私有构造方式，外部不能通过 new 构造新实例
     */
    private StaticInnerClassSingleton() {}

    /**
     * 静态内部类
     * 当外部类 StaticInnerClassSingleton 被加载的时候，并不会创建 SingletonHolder 实例对象。
     * 只有当调用 getInstance() 方法时，SingletonHolder 才会被加载，这个时候才会创建 instance。
     * instance 的唯一性、创建过程的线程安全性，都由 JVM 来保证。
     */
    private static class SingletonHolder {
        /**
         * 私有静态常量的实例化，值在编译期确定，类加载的过程中进行初始化（且只会初始化一次）
         */
        private static final StaticInnerClassSingleton instance = new StaticInnerClassSingleton();
    }

    public static StaticInnerClassSingleton getInstance() {
        return SingletonHolder.instance;
    }

    public long getID() {
        return id.incrementAndGet();
    }
}
