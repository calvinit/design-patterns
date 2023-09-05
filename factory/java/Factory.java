import java.util.HashMap;
import java.util.Map;

interface IReader {
    String read(String path);
}

class XMLReader implements IReader {
    @Override
    public String read(String path) {
        return "xml:" + path;
    }
}

class JSONReader implements IReader {
    @Override
    public String read(String path) {
        return "json:" + path;
    }
}

class YAMLReader implements IReader {
    @Override
    public String read(String path) {
        return "yaml:" + path;
    }
}

// ============================================================

interface IFactory {
    IReader createReader();
}

class XMLReaderFactory implements IFactory {
    @Override
    public IReader createReader() {
        return new XMLReader();
    }
}

class JSONReaderFactory implements IFactory {
    @Override
    public IReader createReader() {
        return new JSONReader();
    }
}

class YAMLReaderFactory implements IFactory {
    @Override
    public IReader createReader() {
        return new YAMLReader();
    }
}

// ============================================================

/**
 * 创建型 - 工厂方法
 * 当对象的创建逻辑比较复杂，不只是简单的 new 一下就可以，而是要组合其他类对象，做各种初始化操作的时候，推荐使用工厂方法模式，
 * 将复杂的创建逻辑拆分到多个工厂类中，让每个工厂类都不至于过于复杂。
 */
public class Factory {

    private static final Map<Integer, IFactory> cachedReaderFactories = new HashMap<>();

    static {
        // 工厂对象复用，缓存起来
        cachedReaderFactories.put(1, new XMLReaderFactory());
        cachedReaderFactories.put(2, new JSONReaderFactory());
        cachedReaderFactories.put(3, new YAMLReaderFactory());
    }

    static IFactory getReaderFactory(int type) {
        return cachedReaderFactories.get(type);
    }
}
