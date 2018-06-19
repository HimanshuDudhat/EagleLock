package cn.jcyh.eaglelock.function.presenter;

import android.text.TextUtils;

import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.constant.Config;
import cn.jcyh.eaglelock.control.ControlCenter;
import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.function.contract.LoginContract;
import cn.jcyh.eaglelock.function.model.LoginModel;
import cn.jcyh.eaglelock.http.MyServerJNI;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.L;
import cn.jcyh.eaglelock.util.T;
import cn.jcyh.eaglelock.util.Tool;
import cn.jcyh.eaglelock.util.Util;
import cn.jcyh.locklib.constant.HttpErrorCode;

/**
 * Created by jogger on 2018/6/7.
 */
public class LoginPresenter extends BasePresenter<LoginContract.View, LoginContract.Model> implements LoginContract.Presenter {

    private final ControlCenter mControlCenter;

    public LoginPresenter() {
        mControlCenter = ControlCenter.getControlCenter();
    }

    @Override
    public void initView() {
        String account = mControlCenter.getAccount();
        String pwd = mControlCenter.getPwd();
        boolean isAutoLogin = mControlCenter.getIsAutoLogin();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
            mView.initView(account.replace(Config.EAGLEKING, ""), pwd, isAutoLogin);
        }
    }

    @Override
    public void login() {
        final String loginUserName = mView.getLoginUserName();
        final String loginPassword = mView.getLoginPassword();
        if (TextUtils.isEmpty(loginUserName) || TextUtils.isEmpty(loginPassword)) {
            T.show(R.string.input_can_not_null);
            return;
        }
        mView.showProgressDialog();
        mModel.login(Config.EAGLEKING + loginUserName, loginPassword, new OnHttpRequestListener<User>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
                mView.cancelProgressDialog();
                T.show(R.string.connect_failure, errorCode);
                L.e("--------errorCode" + errorCode);
            }

            @Override
            public void onSuccess(User user) {
                L.e("----------user:" + user);
                ControlCenter controlCenter = ControlCenter.getControlCenter();
                controlCenter.saveUserInfo(user);
                controlCenter.saveUserAccount(Config.EAGLEKING + loginUserName);
                controlCenter.saveUserPwd(loginPassword);
                if (mView == null) return;
                mView.cancelProgressDialog();
                controlCenter.saveIsAutoLogin(mView.getIsAutoLogin());
                mView.loginSuccess();
            }
        });
    }

    @Override
    public void regist() {
        final String registUserName = mView.getRegistUserName();
        final String registPassword = mView.getRegistPassword();
        int code = mView.getRegistCode();
        if (TextUtils.isEmpty(registUserName) || TextUtils.isEmpty(registPassword) || code == -1) {
            T.show(R.string.input_can_not_null);
            return;
        }
        if (code == -2) {
           T.show(R.string.input_error);
            return;
        }
        mModel.regist(Config.EAGLEKING + registUserName, registPassword, code, new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
                mView.cancelProgressDialog();
            }

            @Override
            public void onSuccess(Boolean b) {
                ControlCenter controlCenter = ControlCenter.getControlCenter();
                controlCenter.saveIsAutoLogin(mView.getIsAutoLogin());
                controlCenter.saveUserAccount(Config.EAGLEKING + registUserName);
                controlCenter.saveUserPwd(registPassword);
                if (mView == null) return;
                mView.cancelProgressDialog();
                mView.registSuccess(registUserName, registPassword);
            }
        });
    }

    @Override
    public void requestRegistCode() {
        String account = mView.getRegistUserName();
        if (TextUtils.isEmpty(account)) {
           T.show(R.string.input_can_not_null);
            return;
        }
        String telRegex = Util.getApp().getString(R.string.regex_phone);
        if (!account.matches(telRegex)) {
           T.show(R.string.phone_no_match);
            return;
        }
        long time = System.currentTimeMillis();
        String sign = Tool.MD5(time + MyServerJNI.getServerKey());
        mModel.sendRegistCode(account, sign, time, new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                L.e("---------onFailure" + errorCode);
                switch (errorCode) {
                    case HttpErrorCode.USER_EXISTS:
                       T.show(R.string.user_registed);
                        break;
                }
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                L.e("---------onSuccess");
                mView.requestRegistCodeSuccess();
            }
        });

    }

    @Override
    public void requestForgetCode() {
        String account = mView.getForgetUserName();
        if (TextUtils.isEmpty(account)) {
           T.show(R.string.input_can_not_null);
            return;
        }
        String telRegex = Util.getApp().getString(R.string.regex_phone);
        if (!account.matches(telRegex)) {
           T.show(R.string.phone_no_match);
            return;
        }
        long time = System.currentTimeMillis();
        String sign = Tool.MD5(time + MyServerJNI.getServerKey());
        mModel.sendForgetCode(account, sign, time, new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                mView.requestForgetCodeSuccess();
            }
        });
    }

    @Override
    public void setAutoLogin(boolean autoLogin) {
        ControlCenter.getControlCenter().saveIsAutoLogin(autoLogin);
    }

    @Override
    public void setBackPassword() {
        final String forgetUserName = mView.getForgetUserName();
        final String forgetPassword = mView.getForgetPassword();
        int code = mView.getRegistCode();
        if (TextUtils.isEmpty(forgetUserName) || TextUtils.isEmpty(forgetPassword) || code == -1) {
           T.show(R.string.input_can_not_null);
            return;
        }
        if (code == -2) {
           T.show(R.string.input_error);
            return;
        }
        mView.showProgressDialog();
        mModel.setBackPassword(forgetUserName, forgetPassword, code, new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                mView.cancelProgressDialog();
                ControlCenter controlCenter = ControlCenter.getControlCenter();
                controlCenter.saveUserAccount(Config.EAGLEKING + forgetUserName);
                controlCenter.saveUserPwd(forgetPassword);
                mView.setBackPasswordSuccess();
            }
        });
    }

    @Override
    public LoginContract.Model attacheModel() {
        return new LoginModel();
    }
}
