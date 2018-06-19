package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.entity.LockKeyboardPwd;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/14.
 */
public interface SendPwdItemContract {
    interface Model extends BaseModel {
        void setLockKey(LockKey lockKey);

        void customPwd(String keyboardPwd, long startTime, long endTime, OnHttpRequestListener<LockKeyboardPwd> listener);

        void addPeriodKeyboardPassword(String keyboardPwd, long startTime, long endTime);

        void getPwd(int keyboardPwdType, long startTime, long endTime, OnHttpRequestListener<LockKeyboardPwd> listener);
    }

    interface View extends IBaseView {
        String getKeyboardPwd();

        long getStartTime();

        long getEndTime();

        void customPwdSuccess();

        void getPwdSuccess(LockKeyboardPwd lockKeyboardPwd);
    }

    interface Presenter extends IPresenter<View, Model> {
        void registerReceiver();

        void addPeriodKeyboardPassword(String keyboardPwd, long startTime, long endTime);

        void getPwd(int keyboardPwdType, long startTime, long endTime);
    }
}
