package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.BaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.http.HttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/7.
 */
public interface LoginContract {
    interface Model extends BaseModel {
        void login(String userName, String pwd, OnHttpRequestListener<HttpResult<User>> listener);
    }

    interface View extends BaseView {
        String getLoginUserName();

        String getLoginPassword();

        String getRegistUserName();

        String getRegistPassword();

        void showNoNullToast();
    }

    interface Presenter extends IPresenter<View, Model> {

        void initView();

        void login();

        void regist();

        void setAutoLogin(boolean autoLogin);
    }
}
