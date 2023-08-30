interface ISimpleReader {
    String read(String path);
}

class XMLReader implements ISimpleReader {
    @Override
    public String read(String path) {
        return "simple-xml:" + path;
    }
}

class JSONReader implements ISimpleReader {
    @Override
    public String read(String path) {
        return "simple-json:" + path;
    }
}

class YAMLReader implements ISimpleReader {
    @Override
    public String read(String path) {
        return "simple-yaml:" + path;
    }
}

/**
 * 创建型 - 简单工厂
 */
public class SimpleFactory {
    static ISimpleReader createReader(int type) {
        // 如果对象可以复用，可以将它缓存起来，这样不需要每次都 new 一个新的实例（有点类似于单例模式和简单工厂模式的结合）
        return switch (type) {
            case 1 -> new XMLReader();
            case 2 -> new JSONReader();
            case 3 -> new YAMLReader();
            default -> throw new IllegalArgumentException("Unknown simple reader type: " + type);
        };
    }
}
