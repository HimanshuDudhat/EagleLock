package cn.jcyh.eaglelock.function.model;

import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.function.contract.LoginContract;
import cn.jcyh.eaglelock.http.UserHttpAction;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/7.
 */
public class LoginModel implements LoginContract.Model {

    private final UserHttpAction mUserHttpAction;

    public LoginModel() {
        mUserHttpAction = UserHttpAction.geHttpAction();
    }

    @Override
    public void sendRegistCode(String userName, String sign, long time, OnHttpRequestListener<Boolean> listener) {
        mUserHttpAction.sendCodeRegist(userName, sign, time, listener);
    }

    @Override
    public void sendForgetCode(String userName, String sign, long time, OnHttpRequestListener<Boolean> listener) {
        mUserHttpAction.sendCodeForget(userName, sign, time, listener);
    }

    @Override
    public void login(String userName, String pwd, OnHttpRequestListener<User> listener) {
        mUserHttpAction.login(userName, pwd, listener);
    }

    @Override
    public void regist(String userName, String pwd, int code, OnHttpRequestListener<Boolean> listener) {
        mUserHttpAction.regist(userName, pwd, code, listener);
    }
}
