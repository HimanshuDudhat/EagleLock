package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/15.
 */
public interface SettingContract {
    interface Model extends BaseModel {
        void setLockKey(LockKey lockKey);

        void rename(String content, OnHttpRequestListener<Boolean> listener);

        void setAdminKeyboardPassword(String content);

        void changeAdminKeyboardPwd(String password, OnHttpRequestListener<Boolean> listener);

        void deleteLockKey();

        void deleteLockKeyFromServer(OnHttpRequestListener<Boolean> listener);
    }

    interface View extends IBaseView {
        void onRenameSuccess(String content);

        void onSetAdminPwdSuccess(String password);

        void onDeleteLockKeySuccess();
    }

    interface Presenter extends IPresenter<View, Model> {
        void registerReceiver();

        void rename(String content);

        void setAdminKeyboardPassword(String content);

        void deleteLockKey();
    }
}
