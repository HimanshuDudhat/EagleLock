package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.LockKey;

/**
 * Created by jogger on 2018/6/15.
 */
public interface LockClockContract {
    interface Model extends BaseModel {
        void setLockKey(LockKey lockKey);

        void getLockTime();

        void setLockTime();
    }

    interface View extends IBaseView {

        void getLockTimeSuccess(String lockDate);

        void setLockTimeSuccess();
    }

    interface Presenter extends IPresenter<View, Model> {
        void registerReceiver();

        void getLockTime();

        void setLockTime();
    }
}
