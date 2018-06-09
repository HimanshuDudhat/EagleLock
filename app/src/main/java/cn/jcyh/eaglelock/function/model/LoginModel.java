package cn.jcyh.eaglelock.function.model;

import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.function.contract.LoginContract;
import cn.jcyh.eaglelock.http.HttpResult;
import cn.jcyh.eaglelock.http.UserHttpAction;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/7.
 */
public class LoginModel implements LoginContract.Model {
    @Override
    public void login(String userName, String pwd,OnHttpRequestListener<HttpResult<User>> listener) {
        UserHttpAction.geHttpAction().login(userName, pwd,listener);
    }
}
