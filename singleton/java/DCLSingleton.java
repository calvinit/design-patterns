import java.util.concurrent.atomic.AtomicLong;

/**
 * 双重检查锁定（Double-Checked Locking）方式（支持延迟加载）
 */
public class DCLSingleton {
    private final AtomicLong id = new AtomicLong(0);
    /**
     * 注意这里需要 volatile 关键字来保证其可见性（禁止指令重排序）
     */
    private static volatile DCLSingleton instance;

    /**
     * 私有构造方式，外部不能通过 new 构造新实例
     */
    private DCLSingleton() {}

    public static DCLSingleton getInstance() {
        if (instance == null) { // 第一次检查
            synchronized (DCLSingleton.class) { // 此处为类级别的锁
                if (instance == null) { // 第二次检查
                    // CPU 指令重排序可能导致在 DCLSingleton 类的对象被关键字 new 创建并赋值给 instance 之后，
                    // 还没来得及初始化（执行构造函数中的代码逻辑），就被另一个线程使用了。
                    // 这样，另一个线程就使用了一个没有完整初始化的 DCLSingleton 类的对象。
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }

    public long getID() {
        return id.incrementAndGet();
    }
}
