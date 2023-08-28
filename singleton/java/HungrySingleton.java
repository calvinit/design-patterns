import java.util.concurrent.atomic.AtomicLong;

/**
 * 饿汉式（不支持延迟加载）
 */
public class HungrySingleton {
    private final AtomicLong id = new AtomicLong(0);
    /**
     * 私有静态常量的实例化，值在编译期确定，类加载的过程中进行初始化（且只会初始化一次）
     */
    private static final HungrySingleton instance = new HungrySingleton();

    /**
     * 私有构造方式，外部不能通过 new 构造新实例
     */
    private HungrySingleton() {}

    /**
     * 对外暴露获取单例实例的静态公开方法
     */
    public static HungrySingleton getInstance() {
        return instance;
    }

    public long getID() {
        return id.incrementAndGet();
    }
}
