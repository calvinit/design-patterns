public class ProxyTest {
    public static void main(String[] args) {
        // 1. 对于原始类有定义了接口的“静态代理”（组合模式）
        IBiz1 biz1 = new StaticProxy1(new Biz1Impl());
        biz1.justDoIt();

        // 2. 对于原始类没有定义接口且原始类代码并不是我们维护的（比如它来自一个第三方的类库）的“静态代理”（继承模式）
        Biz2 biz2 = new StaticProxy2();
        biz2.justDoIt();

        // 3. “动态代理”（防止过多模板式“重复”代码的代理类，增加代码维护成本），运行时动态地创建原始类对应的代理类，然后在系统中使用代理类替换掉原始类（参考 Spring AOP 底层实现）
        DynamicProxy proxy = new DynamicProxy();
        // 注意 Java 动态代理只能针对接口实现代理，否则会报异常：java.lang.ClassCastException: class jdk.proxy1.$Proxy0 cannot be cast to class Xxx (jdk.proxy1.$Proxy0 is in module jdk.proxy1 of loader 'app'; Xxx is in unnamed module of loader 'app')
        IBiz3 biz3 = (IBiz3) proxy.createProxy(new Biz3Impl());
        biz3.justDoIt();
    }
}
