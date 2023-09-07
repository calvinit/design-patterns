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


        IAbstractFactory abstractFactory1 = AbstractFactory.getReaderFactory(1);
        IAbstractFactory abstractFactory2 = AbstractFactory.getReaderFactory(2);
        IAbstractFactory abstractFactory3 = AbstractFactory.getReaderFactory(3);
        IJavaReader javaReader1 = abstractFactory1.createJavaReader();
        IJavaReader javaReader2 = abstractFactory2.createJavaReader();
        IJavaReader javaReader3 = abstractFactory3.createJavaReader();
        IKotlinReader kotlinReader1 = abstractFactory1.createKotlinReader();
        IKotlinReader kotlinReader2 = abstractFactory2.createKotlinReader();
        IKotlinReader kotlinReader3 = abstractFactory3.createKotlinReader();
        IScalaReader scalaReader1 = abstractFactory1.createScalaReader();
        IScalaReader scalaReader2 = abstractFactory2.createScalaReader();
        IScalaReader scalaReader3 = abstractFactory3.createScalaReader();
        System.out.printf("3.1 抽象工厂: java_reader(1)=>%s, java_reader(2)=>%s, java_reader(3)=>%s%n",
                javaReader1.read("/path/to/xml-file"), javaReader2.read("/path/to/json-file"), javaReader3.read("/path/to/yaml-file"));
        System.out.printf("3.2 抽象工厂: kotlin_reader(1)=>%s, kotlin_reader(2)=>%s, kotlin_reader(3)=>%s%n",
                kotlinReader1.read("/path/to/xml-file"), kotlinReader2.read("/path/to/json-file"), kotlinReader3.read("/path/to/yaml-file"));
        System.out.printf("3.3 抽象工厂: scala_reader(1)=>%s, scala_reader(2)=>%s, scala_reader(3)=>%s%n",
                scalaReader1.read("/path/to/xml-file"), scalaReader2.read("/path/to/json-file"), scalaReader3.read("/path/to/yaml-file"));
    }
}