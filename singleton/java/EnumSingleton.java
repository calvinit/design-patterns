import java.util.concurrent.atomic.AtomicLong;

/**
 * 枚举式（支持延迟加载）
 */
public enum EnumSingleton {
    INSTANCE;
    private final AtomicLong id = new AtomicLong(0);

    public long getID() {
        return id.incrementAndGet();
    }
}
