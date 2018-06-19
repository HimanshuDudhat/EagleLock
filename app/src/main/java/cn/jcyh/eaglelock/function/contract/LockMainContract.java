package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.locklib.entity.Error;

/**
 * Created by jogger on 2018/6/13.
 */
public interface LockMainContract {
    interface Model extends BaseModel {
        void setLockKey(LockKey lockKey);

        void unlock();
    }

    interface View extends IBaseView {
        void unLockResult(Error error);
    }

    interface Presenter extends IPresenter<View, Model> {
        void registerReceiver();

        void unlock();

    }
}
