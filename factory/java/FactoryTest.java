public class FactoryTest {
    public static void main(String[] args) {
        ISimpleReader simpleReader1 = SimpleFactory.createReader(1);
        ISimpleReader simpleReader2 = SimpleFactory.createReader(2);
        ISimpleReader simpleReader3 = SimpleFactory.createReader(3);
        System.out.printf("1. 简单工厂: reader(1)=>%s, reader(2)=>%s, reader(3)=>%s%n",
                simpleReader1.read("/path/to/xml-file"), simpleReader2.read("/path/to/json-file"), simpleReader3.read("/path/to/yaml-file"));

        IFactory factory1 = Factory.getReaderFactory(1);
        IFactory factory2 = Factory.getReaderFactory(2);
        IFactory factory3 = Factory.getReaderFactory(3);
        IReader reader1 = factory1.createReader();
        IReader reader2 = factory2.createReader();
        IReader reader3 = factory3.createReader();
        System.out.printf("2. 工厂方法: reader(1)=>%s, reader(2)=>%s, reader(3)=>%s%n",
                reader1.read("/path/to/xml-file"), reader2.read("/path/to/json-file"), reader3.read("/path/to/yaml-file"));
    }
}