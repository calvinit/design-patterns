public class SingletonTest {
    @SuppressWarnings({"ObjectEquality", "ConstantValue"})
    public static void main(String[] args) {
        // 1. 饿汉式
        HungrySingleton hungry1 = HungrySingleton.getInstance();
        HungrySingleton hungry2 = HungrySingleton.getInstance();
        // getInstance() 两次获取得到的实例应该是一样的，所以 hungry1 == hungry2 应该为 true
        System.out.printf("1. 饿汉式: (1)=%s, (2)=%s, (1==2?)=%b, (1.id)=%d, (2.id)=%d%n",
                hungry1, hungry2, hungry1 == hungry2, hungry1.getID(), hungry2.getID());

        // 2. 懒汉式 - synchronized
        LazySingleton lazy1 = LazySingleton.getInstance();
        LazySingleton lazy2 = LazySingleton.getInstance();
        // getInstance() 两次获取得到的实例应该是一样的，所以 lazy1 == lazy2 应该为 true
        System.out.printf("2. 懒汉式: (1)=%s, (2)=%s, (1==2?)=%b, (1.id)=%d, (2.id)=%d%n",
                lazy1, lazy2, lazy1 == lazy2, lazy1.getID(), lazy2.getID());

        // 3. DCL 双重检查锁定
        DCLSingleton dcl1 = DCLSingleton.getInstance();
        DCLSingleton dcl2 = DCLSingleton.getInstance();
        // getInstance() 两次获取得到的实例应该是一样的，所以 dcl1 == dcl2 应该为 true
        System.out.printf("3. 双重检查锁定: (1)=%s, (2)=%s, (1==2?)=%b, (1.id)=%d, (2.id)=%d%n",
                dcl1, dcl2, dcl1 == dcl2, dcl1.getID(), dcl2.getID());

        // 4. 静态内部类
        StaticInnerClassSingleton sic1 = StaticInnerClassSingleton.getInstance();
        StaticInnerClassSingleton sic2 = StaticInnerClassSingleton.getInstance();
        // getInstance() 两次获取得到的实例应该是一样的，所以 sic1 == sic2 应该为 true
        System.out.printf("4. 静态内部类: (1)=%s, (2)=%s, (1==2?)=%b, (1.id)=%d, (2.id)=%d%n",
                sic1, sic2, sic1 == sic2, sic1.getID(), sic2.getID());

        // 5. 枚举
        EnumSingleton enum1 = EnumSingleton.INSTANCE;
        EnumSingleton enum2 = EnumSingleton.INSTANCE;
        // getInstance() 两次获取得到的实例应该是一样的，所以 enum1 == enum2 应该为 true
        System.out.printf("5. 枚举: (1)=%s, (2)=%s, (1==2?)=%b, (1.id)=%d, (2.id)=%d%n",
                enum1.getClass().getName() + "@" + Integer.toHexString(enum1.hashCode()),
                enum2.getClass().getName() + "@" + Integer.toHexString(enum2.hashCode()),
                enum1 == enum2, enum1.getID(), enum2.getID());
    }
}
