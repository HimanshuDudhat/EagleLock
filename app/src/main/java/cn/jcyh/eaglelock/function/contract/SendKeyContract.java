package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

public interface SendKeyContract {
    interface Model extends BaseModel {
        void setLockKey(LockKey lockKey);

        void sendKey(String account, long startTime, long endTime, String remarks, OnHttpRequestListener<Boolean> listener);
    }

    interface View extends IBaseView {

        long getStartTime();

        long getEndTime();

        String getReceiveAccount();

        int getCurrentType();

        void sendKeySuccess();
    }

    interface Presenter extends IPresenter<View, Model> {
        void sendKey();
    }
}
