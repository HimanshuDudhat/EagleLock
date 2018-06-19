package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

public interface KeyManageContract {
    interface Model extends BaseModel {
        void setLockKey(LockKey lockKey);

        void getLockKeys(int pageNo, int pageSize, OnHttpRequestListener<LockHttpResult<LockKey>> listener);

        void resetKey();

        void resetKeyFromServer(OnHttpRequestListener<Boolean> listener);

        void clearKey(OnHttpRequestListener<Boolean> listener);
    }

    interface View extends IBaseView {
        void getLockKeysSuccess(LockHttpResult<LockKey> lockKeyHttpResult);

        void resetKeySuccess();

        void clearKeySuccess();
    }

    interface Presenter extends IPresenter<View, Model> {
        void registerReceiver();

        void getLockKeys(int pageNo, int pageSize);

        void resetKey();

        void clearKey();
    }
}
