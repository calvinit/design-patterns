public class FactoryTest {
    public static void main(String[] args) {
        ISimpleReader reader1 = SimpleFactory.createReader(1);
        ISimpleReader reader2 = SimpleFactory.createReader(2);
        ISimpleReader reader3 = SimpleFactory.createReader(3);
        System.out.printf("1. 简单工厂: reader(1)=>%s, reader(2)=>%s, reader(3)=>%s%n",
                reader1.read("/path/to/xml-file"), reader2.read("/path/to/json-file"), reader3.read("/path/to/yaml-file"));
    }
}