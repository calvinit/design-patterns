public class DecoratorTest {
    public static void main(String[] args) {
        String writtenNeed = "这是需要写入的内容数据。！@#123456ABCabcQWErty";
        // 这里装饰了 2 层，最终核心实现是委托给 FileDataSource 执行，装饰器在其上对写入和读取数据功能进行增强
        // 根据数据在装饰器栈的流向，可以总结出：
        // 1. 对于写入数据，顺序分别是：对需要写入的内容数据（writtenNeed）进行压缩 -> 再进行加密 -> 最终写入文件
        // 2. 对于读取数据，顺序分别是：读取文件内容 -> 再进行解密 -> 最终解压内容数据
        DataSourceDecorator decorator = new CompressionDecorator(new EncryptionDecorator(new FileDataSource("path/to/read-and-write.txt")));
        // 写入内容
        decorator.write(writtenNeed);
        // 读取内容
        String fileContent = decorator.read();
        System.out.println("读取到的文件内容为：" + fileContent);

        // ==> 以上代码控制台输出内容如下：
        // FileDataSource 写入到文件 path/to/read-and-write.txt 中，数据为：Zkt4Q1B4RUYwLGowbmZiWnMsbmRoUGpuaGZYSG5mWEdxZmZiaVBYSGlmWHZ2ZmJXdFBiT3N2UEJodiw5aFZCa05VSntPRVYzUlZLRVpYS2tWV2VHZG9TNmZiVmlCeD4+
        // FileDataSource 读取文件 path/to/read-and-write.txt
        // 读取到的文件内容为：这是需要写入的内容数据。！@#123456ABCabcQWErty
    }
}
