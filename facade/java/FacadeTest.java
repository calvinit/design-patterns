public class FacadeTest {
    public static void main(String[] args) {
        String phone = "13800138000";
        String vcode = "123321";

        Facade facade = new Facade();
        boolean flag = facade.loginOrRegister(phone, vcode);
        if (flag) {
            System.out.println("【登录或注册】成功");
        } else {
            System.err.println("【登录或注册】失败");
        }

        if (facade.userService().login(phone, vcode)) {
            System.out.println("【登录或注册】重新登录成功");
        } else {
            System.err.println("【登录或注册】重新登录失败");
        }
    }
}
