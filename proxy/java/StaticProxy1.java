interface IBiz1 {
    void justDoIt();
}

class Biz1Impl implements IBiz1 {
    @Override
    public void justDoIt() {
        System.out.println("1. ==> Do it in Biz1Impl.");
    }
}

@SuppressWarnings("ClassEscapesDefinedScope")
public class StaticProxy1 implements IBiz1 {

    private final Biz1Impl biz1;

    public StaticProxy1(Biz1Impl biz1) {
        this.biz1 = biz1;
    }

    @Override
    public void justDoIt() {
        System.out.println("1. Proxy: before biz1.justDoIt().");
        biz1.justDoIt();
        System.out.println("1. Proxy: after biz1.justDoIt().");
    }
}
