import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * 定义了读取和写入操作的通用数据接口
 */
interface DataSource {
    String read();

    void write(String data);
}

/**
 * 简单数据读写器
 */
class FileDataSource implements DataSource {
    private final String name;
    /**
     * 写入到文件的内容（这里是模拟，并没有真正写入到文件中）
     */
    private String fileContent = "";

    public FileDataSource(String name) {
        this.name = name;
    }

    @Override
    public String read() {
        System.out.println("FileDataSource 读取文件 " + name);
        return fileContent;
    }

    @Override
    public void write(String data) {
        fileContent = data; // 模拟写入文件中
        System.out.println("FileDataSource 写入到文件 " + name + " 中，数据为：" + data);
    }
}

// ============================================================

/**
 * 抽象基础装饰，这里实现 DataSource 是为了可以给其他装饰器再次装饰（甚至可以递归装饰）
 */
class DataSourceDecorator implements DataSource {
    private final DataSource delegate;

    public DataSourceDecorator(DataSource source) {
        this.delegate = source;
    }

    @Override
    public String read() {
        return delegate.read();
    }

    @Override
    public void write(String data) {
        delegate.write(data);
    }
}

/**
 * 具体装饰实现 - 加解密装饰
 */
class EncryptionDecorator extends DataSourceDecorator {
    public EncryptionDecorator(DataSource source) {
        super(source);
    }

    @Override
    public String read() {
        return decode(super.read());
    }

    @Override
    public void write(String data) {
        super.write(encode(data));
    }

    private String encode(String data) {
        byte[] result = data.getBytes();
        for (int i = 0; i < result.length; i++) {
            result[i] += (byte) 1;
        }
        return Base64.getEncoder().encodeToString(result);
    }

    private String decode(String data) {
        byte[] result = Base64.getDecoder().decode(data);
        for (int i = 0; i < result.length; i++) {
            result[i] -= (byte) 1;
        }
        return new String(result);
    }
}

/**
 * 具体装饰实现 - 压缩/解压装饰
 */
class CompressionDecorator extends DataSourceDecorator {
    private int compLevel = Deflater.DEFAULT_COMPRESSION;

    public CompressionDecorator(DataSource source) {
        super(source);
    }

    public int getCompressionLevel() {
        return compLevel;
    }

    public void setCompressionLevel(int value) {
        compLevel = value;
    }

    @Override
    public void write(String data) {
        super.write(compress(data));
    }

    @Override
    public String read() {
        return decompress(super.read());
    }

    private String compress(String src) {
        byte[] bytesData = src.getBytes();
        try {
            // 这里的 OutputStream、ByteArrayOutputStream、DeflaterOutputStream 其实也是用了装饰器模式
            // 注意这里不能使用 try-with-resources 语法，否则会报 java.io.EOFException: Unexpected end of ZLIB input stream
            ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
            DeflaterOutputStream dos = new DeflaterOutputStream(bout, new Deflater(compLevel));
            dos.write(bytesData);
            dos.close();
            bout.close();
            return Base64.getEncoder().encodeToString(bout.toByteArray());
        } catch (IOException ignore) {
            return null;
        }
    }

    private String decompress(String src) {
        byte[] decodedData = Base64.getDecoder().decode(src);
        try {
            // 这里的 InputStream、ByteArrayInputStream、InflaterInputStream 其实也是用了装饰器模式
            // 注意这里不能使用 try-with-resources 语法，否则会报 java.io.EOFException: Unexpected end of ZLIB input stream
            InputStream in = new ByteArrayInputStream(decodedData);
            InflaterInputStream iin = new InflaterInputStream(in);
            ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
            int b;
            while ((b = iin.read()) != -1) {
                bout.write(b);
            }
            bout.close();
            iin.close();
            in.close();
            return bout.toString();
        } catch (IOException ignore) {
            return null;
        }
    }
}

// 可以有更多的具体装饰实现...