public class BridgeTest {
    public static void main(String[] args) {
        // 桥接模式，GoF 将之定义为：将抽象和实现解耦，让它们可以独立变化。
        // 它将类个数由 M*N 变成 M+N，减少了类数量膨胀（通过组合关系来替代继承关系，避免继承层次的指数级爆炸），增加了灵活性（组合两端可分别独立变化和扩展，且可随意组合）。

        Epson epson = new Epson();
        Hp hp = new Hp();

        // 使用 HP 打印机的 Mac 电脑
        Mac mac = new Mac(hp);
        mac.print();

        // 使用 HP 打印机的 Linux 电脑
        Linux linux = new Linux(hp);
        linux.print();

        // 使用 Epson 打印机的 Linux 电脑
        Windows windows = new Windows(epson);
        windows.print();
    }
}
