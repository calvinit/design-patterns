import java.util.HashMap;
import java.util.Map;

interface IJavaReader {
    String read(String path);
}

interface IKotlinReader {
    String read(String path);
}

interface IScalaReader {
    String read(String path);
}

class XMLJavaReader implements IJavaReader {
    @Override
    public String read(String path) {
        return "(java) xml:" + path;
    }
}

class XMLKotlinReader implements IKotlinReader {
    @Override
    public String read(String path) {
        return "(kotlin) xml:" + path;
    }
}

class XMLScalaReader implements IScalaReader {
    @Override
    public String read(String path) {
        return "(scala) xml:" + path;
    }
}

class JSONJavaReader implements IJavaReader {
    @Override
    public String read(String path) {
        return "(java) json:" + path;
    }
}

class JSONKotlinReader implements IKotlinReader {
    @Override
    public String read(String path) {
        return "(kotlin) json:" + path;
    }
}

class JSONScalaReader implements IScalaReader {
    @Override
    public String read(String path) {
        return "(scala) json:" + path;
    }
}

class YAMLJavaReader implements IJavaReader {
    @Override
    public String read(String path) {
        return "(java) yaml:" + path;
    }
}

class YAMLKotlinReader implements IKotlinReader {
    @Override
    public String read(String path) {
        return "(kotlin) yaml:" + path;
    }
}

class YAMLScalaReader implements IScalaReader {
    @Override
    public String read(String path) {
        return "(scala) yaml:" + path;
    }
}

// ============================================================

interface IAbstractFactory {
    IJavaReader createJavaReader();

    IKotlinReader createKotlinReader();

    IScalaReader createScalaReader();
}

class XMLReaderAbstractFactory implements IAbstractFactory {
    @Override
    public IJavaReader createJavaReader() {
        return new XMLJavaReader();
    }

    @Override
    public IKotlinReader createKotlinReader() {
        return new XMLKotlinReader();
    }

    @Override
    public IScalaReader createScalaReader() {
        return new XMLScalaReader();
    }
}

class JSONReaderAbstractFactory implements IAbstractFactory {
    @Override
    public IJavaReader createJavaReader() {
        return new JSONJavaReader();
    }

    @Override
    public IKotlinReader createKotlinReader() {
        return new JSONKotlinReader();
    }

    @Override
    public IScalaReader createScalaReader() {
        return new JSONScalaReader();
    }
}

class YAMLReaderAbstractFactory implements IAbstractFactory {
    @Override
    public IJavaReader createJavaReader() {
        return new YAMLJavaReader();
    }

    @Override
    public IKotlinReader createKotlinReader() {
        return new YAMLKotlinReader();
    }

    @Override
    public IScalaReader createScalaReader() {
        return new YAMLScalaReader();
    }
}

// ============================================================

/**
 * 创建型 - 抽象工厂
 */
public class AbstractFactory {

    private static final Map<Integer, IAbstractFactory> cachedReaderAbstractFactories = new HashMap<>();

    static {
        // 工厂对象复用，缓存起来
        cachedReaderAbstractFactories.put(1, new XMLReaderAbstractFactory());
        cachedReaderAbstractFactories.put(2, new JSONReaderAbstractFactory());
        cachedReaderAbstractFactories.put(3, new YAMLReaderAbstractFactory());
    }

    static IAbstractFactory getReaderFactory(int type) {
        return cachedReaderAbstractFactories.get(type);
    }
}
