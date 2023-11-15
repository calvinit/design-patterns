package facade

import "fmt"

type User struct {
	Nick  string
	Phone string
}

type IUserService interface {
	Login(phone, vcode string) (bool, error)
	Register(phone, vcode string) (bool, error)
}

type UserServiceImpl struct {
	users map[string]*User
}

func (u UserServiceImpl) Login(phone, vcode string) (bool, error) {
	// 校验手机验证码...
	fmt.Printf("【登录】校验手机验证码 [%s]，校验通过\n", vcode)

	if _, exists := u.users[phone]; !exists {
		// _, _ = fmt.Fprintf(os.Stderr, "【登录】[%s] 手机未注册，登录失败\n", phone)
		return false, nil
	}

	fmt.Printf("【登录】[%s] 登录成功\n", phone)
	return true, nil
}

func (u UserServiceImpl) Register(phone, vcode string) (bool, error) {
	// 校验手机验证码...
	fmt.Printf("【注册】校验手机验证码 [%s]，校验通过\n", vcode)

	if _, exists := u.users[phone]; exists {
		// _, _ = fmt.Fprintf(os.Stderr, "【注册】[%s] 手机已注册，注册失败\n", phone)
		return false, nil
	}

	fmt.Printf("【注册】[%s] 注册成功\n", phone)
	u.users[phone] = &User{"random-nick-name", phone}
	return true, nil
}

type Facade struct {
	u IUserService
}

func NewFacade() *Facade {
	return &Facade{UserServiceImpl{make(map[string]*User)}}
}

// LoginOrRegister 登录或注册
func (f *Facade) LoginOrRegister(phone, vcode string) (bool, error) {
	// 这里其实还需要区分出是“手机未注册”才走注册逻辑，否则返回 false 登录失败
	if ok, err := f.u.Login(phone, vcode); ok && err == nil {
		return true, nil
	}
	return f.u.Register(phone, vcode)
}

// UserService Get IUserService implement. Only for tests!
func (f *Facade) UserService() IUserService {
	return f.u
}
