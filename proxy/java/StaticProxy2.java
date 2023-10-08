class Biz2 {
    public void justDoIt() {
        System.out.println("2. ==> Do it in Biz2.");
    }
}

public class StaticProxy2 extends Biz2 {

    @Override
    public void justDoIt() {
        System.out.println("2. Proxy: before biz2.justDoIt().");
        super.justDoIt();
        System.out.println("2. Proxy: after biz2.justDoIt().");
    }
}
