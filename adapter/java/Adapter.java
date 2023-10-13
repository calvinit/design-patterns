/**
 * 需要适配的目标接口
 */
interface ITarget {
    void f1();

    void f2();

    void fc();
}

// ============================================================

// 不兼容的 Adaptee，我们无法修改（或基于一些原因尽量不要去修改）它
class Adaptee {
    public void fa() {
        System.out.println("I'm Adaptee.fa()");
    }

    public void fb() {
        System.out.println("I'm Adaptee.fb()");
    }

    public void fc() {
        System.out.println("I'm Adaptee.fc()");
    }
}

// ============================================================

/**
 * 类适配器（基于继承方式实现）
 */
class Adapter1 extends Adaptee implements ITarget {
    @Override
    public void f1() {
        System.out.println("I'm Adapter1.f1(), I adapted Adaptee.fa().");
        super.fa();
    }

    @Override
    public void f2() {
        System.out.println("I'm Adapter1.f2(), I adapted Adaptee.fb().");
        super.fb();
    }

    // 这里 fc() 不需要实现，直接继承自 Adaptee，这是跟对象适配器最大的不同点
}

// ============================================================

/**
 * 对象适配器（基于组合方式实现）
 */
class Adapter2 implements ITarget {
    private final Adaptee adaptee;

    public Adapter2(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void f1() {
        System.out.println("I'm Adapter2.f1(), I adapted Adaptee.fa().");
        adaptee.fa();
    }

    @Override
    public void f2() {
        System.out.println("I'm Adapter2.f2(), I adapted Adaptee.fb().");
        adaptee.fb();
    }

    @Override
    public void fc() {
        System.out.println("I'm Adapter2.fc(), I adapted Adaptee.fc().");
        adaptee.fc();
    }
}

/*
 * 针对这两种实现方式，在实际的开发中，到底该如何选择使用哪一种呢？判断的标准主要有两个，一个是 Adaptee 接口的个数，另一个是 Adaptee 和 ITarget 的契合程度：
 * 1、如果 Adaptee 接口并不多，那两种实现方式都可以；
 * 2、如果 Adaptee 接口很多，而且 Adaptee 和 ITarget 接口定义大部分都相同，那我们推荐使用类适配器，因为 Adapter 复用父类 Adaptee 的接口，比起对象适配器的实现方式，Adapter 的代码量要少一些；
 * 3、如果 Adaptee 接口很多，而且 Adaptee 和 ITarget 接口定义大部分都不相同，那我们推荐使用对象适配器，因为组合结构相对于继承更加灵活。
 *
 * 适配器模式应用场景：
 * 1、封装有缺陷的接口设计；
 * 2、统一多个类的接口设计；
 * 3、替换依赖的外部系统；
 * 4、兼容老版本接口；
 * 5、适配不同格式的数据。
 *
 * 代理、桥接、装饰器、适配器 4 种设计模式的区别：
 * 代理、桥接、装饰器、适配器，这 4 种模式是比较常用的结构型设计模式。它们的代码结构非常相似。
 * 笼统来说，它们都可以称为 Wrapper 模式，也就是通过 Wrapper 类二次封装原始类。
 * 尽管代码结构相似，但这 4 种设计模式的用意完全不同，也就是说要解决的问题、应用场景不同，这也是它们的主要区别。
 * 简单总结一下它们之间的区别：
 * 1、代理模式：代理模式在不改变原始类接口的条件下，为原始类定义一个代理类，主要目的是控制访问，而非加强功能，这是它跟装饰器模式最大的不同；
 * 2、桥接模式：桥接模式的目的是将接口部分和实现部分分离，从而让它们可以较为容易、也相对独立地加以改变；
 * 3、装饰器模式：装饰者模式在不改变原始类接口的情况下，对原始类功能进行增强，并且支持多个装饰器的嵌套使用；
 * 4、适配器模式：适配器模式是一种事后的补救策略，适配器提供跟原始类不同的接口，而代理模式、装饰器模式提供的都是跟原始类相同的接口。
 */