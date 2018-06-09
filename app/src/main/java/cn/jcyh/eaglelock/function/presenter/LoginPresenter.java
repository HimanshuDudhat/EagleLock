package cn.jcyh.eaglelock.function.presenter;

import android.text.TextUtils;

import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.control.ControlCenter;
import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.function.contract.LoginContract;
import cn.jcyh.eaglelock.http.HttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.L;

/**
 * Created by jogger on 2018/6/7.
 */
public class LoginPresenter extends BasePresenter<LoginContract.View, LoginContract.Model> implements LoginContract.Presenter {

    @Override
    public void login() {
        String loginUserName = mView.getLoginUserName();
        String loginPassword = mView.getLoginPassword();
        if (TextUtils.isEmpty(loginUserName) || TextUtils.isEmpty(loginPassword)) {
            mView.showNoNullToast();
            return;
        }
        mModel.login(loginUserName, loginPassword, new OnHttpRequestListener<HttpResult<User>>() {
            @Override
            public void onFailure(int errorCode) {

            }

            @Override
            public void onSuccess(HttpResult<User> userHttpResult) {
                L.e("----------userHttpResult:" + userHttpResult);
            }
        });
    }

    @Override
    public void regist() {

    }

    @Override
    public void setAutoLogin(boolean autoLogin) {
        ControlCenter.getControlCenter().saveIsAutoLogin(autoLogin);
    }

}
