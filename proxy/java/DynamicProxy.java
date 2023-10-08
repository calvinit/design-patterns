import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface IBiz3 {
    void justDoIt();
}

class Biz3Impl implements IBiz3 {
    @Override
    public void justDoIt() {
        System.out.println("3. ==> Do it in Biz3Impl.");
    }
}

public class DynamicProxy {
    public Object createProxy(Object proxiedObject) {
        // 获取“被代理类”直接实现的所有接口数组
        Class<?>[] interfaces = proxiedObject.getClass().getInterfaces();
        DynamicProxyHandler handler = new DynamicProxyHandler(proxiedObject);
        return Proxy.newProxyInstance(proxiedObject.getClass().getClassLoader(), interfaces, handler);
    }

    private record DynamicProxyHandler(Object proxiedObject) implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("3. Proxy: before dynamic-proxy method.invoke(proxiedObject, args).");
            Object result = method.invoke(proxiedObject, args);
            System.out.println("3. Proxy: after dynamic-proxy method.invoke(proxiedObject, args).");
            return result;
        }
    }
}

