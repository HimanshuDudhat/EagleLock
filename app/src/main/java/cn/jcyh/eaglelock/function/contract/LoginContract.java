package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/7.
 */
public interface LoginContract {
    interface Model extends BaseModel {
        void sendRegistCode(String userName, String sign, long time, OnHttpRequestListener<Boolean> listener);

        void sendForgetCode(String userName, String sign, long time, OnHttpRequestListener<Boolean> listener);

        void login(String userName, String pwd, OnHttpRequestListener<User> listener);

        void regist(String userName, String pwd, int code, OnHttpRequestListener<Boolean> listener);

    }

    interface View extends IBaseView {
        void initView(String account, String pwd, boolean isAutoLogin);

        String getLoginUserName();

        String getLoginPassword();

        boolean getIsAutoLogin();

        String getRegistUserName();

        String getRegistPassword();

        int getRegistCode();

        void requestRegistCodeSuccess();

        void registSuccess(String registUserName, String registPassword);

        String getForgetUserName();

        String getForgetPassword();

        int getForgetCode();

        void requestForgetCodeSuccess();

        void loginSuccess();

    }

    interface Presenter extends IPresenter<View, Model> {

        void initView();

        void login();

        void regist();

        void requestRegistCode();

        void requestForgetCode();

        void setAutoLogin(boolean autoLogin);
    }
}
