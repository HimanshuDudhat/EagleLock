package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.entity.LockPwdRecord;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/19.
 */
public interface PwdManageContract {
    interface Model extends BaseModel {
        void setLockKey(LockKey lockKey);

        void getPwdRecords(int pageNo, int pageSize, OnHttpRequestListener<LockHttpResult<LockPwdRecord>> listener);

        void resetPwd();

        void resetFromServer(String pwdInfo, long timestamp, OnHttpRequestListener<Boolean> listener);
    }

    interface View extends IBaseView {
        void getPwdRecordsSuccess(LockHttpResult<LockPwdRecord> lockKeyHttpResult);

        void resetPwdSuccess();
    }

    interface Presenter extends IPresenter<PwdManageContract.View, PwdManageContract.Model> {
        void registerReceiver();

        void getPwdRecords(int pageNo, int pageSize);

        void resetPwd();
    }
}
