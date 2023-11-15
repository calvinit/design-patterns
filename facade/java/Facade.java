import java.util.HashMap;
import java.util.Map;

class User {
    private String nick;
    private String phone;

    public User(String nick, String phone) {
        this.nick = nick;
        this.phone = phone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

interface IUserService {
    boolean login(String phone, String vcode);

    boolean register(String phone, String vcode);
}

class UserServiceImpl implements IUserService {

    private static final Map<String, User> users = new HashMap<>();

    @Override
    public boolean login(String phone, String vcode) {
        // 校验手机验证码...
        System.out.printf("【登录】校验手机验证码 [%s]，校验通过\n", vcode);

        if (!users.containsKey(phone)) {
            // System.err.printf("【登录】[%s] 手机未注册，登录失败\n", phone);
            return false;
        }

        System.out.printf("【登录】[%s] 登录成功\n", phone);
        return true;
    }

    @Override
    public boolean register(String phone, String vcode) {
        // 校验手机验证码...
        System.out.printf("【注册】校验手机验证码 [%s]，校验通过\n", vcode);

        if (users.containsKey(phone)) {
            // System.err.printf("【注册】[%s] 手机已注册，注册失败\n", phone);
            return false;
        }

        System.out.printf("【注册】[%s] 注册成功\n", phone);
        users.put(phone, new User("random-nick-name", phone));
        return true;
    }
}

public class Facade {

    private final IUserService userService = new UserServiceImpl();

    // 登录或注册
    public boolean loginOrRegister(String phone, String vcode) {
        // 这里其实还需要区分出是“手机未注册”才走注册逻辑，否则返回 false 登录失败
        if (userService.login(phone, vcode)) {
            return true;
        }
        return userService.register(phone, vcode);
    }

    // Get IUserService implement. Only for tests!
    @SuppressWarnings("ClassEscapesDefinedScope")
    public IUserService userService() {
        return this.userService;
    }
}
