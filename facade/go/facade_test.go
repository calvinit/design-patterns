package facade

import "testing"

func TestFacade(t *testing.T) {
	phone, vcode := "13800138000", "123321"

	f := NewFacade()
	if ok, err := f.LoginOrRegister(phone, vcode); ok && err == nil {
		t.Log("【登录或注册】成功")
	} else {
		t.Error("【登录或注册】失败")
	}

	if ok, err := f.UserService().Login(phone, vcode); ok && err == nil {
		t.Log("【登录或注册】重新登录成功")
	} else {
		t.Error("【登录或注册】重新登录失败")
	}
}
