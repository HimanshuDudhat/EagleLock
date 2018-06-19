package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/19.
 */
public interface KeyInfoContract {
    interface Model extends BaseModel {
        public void setLockKey(LockKey lockKey);

        void rename(String name, OnHttpRequestListener<Boolean> listener);

        void authKeyUser(OnHttpRequestListener<Boolean> listener);

        void unAuthKeyUser(OnHttpRequestListener<Boolean> listener);

        void freezeKey(OnHttpRequestListener<Boolean> listener);

        void unFreezeKey(OnHttpRequestListener<Boolean> listener);

        void deleteKey(OnHttpRequestListener<Boolean> listener);
    }

    interface View extends IBaseView {
        void renameSuccess();

        void authKeyUserSuccess();

        void unAuthKeyUserSuccess();

        void freezeKeySuccess();

        void unFreezeKeySuccess();

        void deleteKeySuccess();
    }

    interface Presenter extends IPresenter<View, Model> {
        void rename(String name);

        void authKeyUser();

        void unAuthKeyUser();

        void freezeKey();

        void unFreezeKey();

        void deleteKey();
    }
}
