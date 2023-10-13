public class AdapterTest {
    public static void main(String[] args) {
        ITarget target = new Adapter1();
        target.f1();
        target.f2();
        target.fc();

        System.out.println("==========================================");

        target = new Adapter2(new Adaptee());
        target.f1();
        target.f2();
        target.fc();
    }
}
