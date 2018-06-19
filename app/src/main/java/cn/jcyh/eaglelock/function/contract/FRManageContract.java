package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.locklib.entity.FR;

/**
 * Created by jogger on 2018/6/19.
 */
public interface FRManageContract {
    interface Model extends BaseModel {
        void setLockKey(LockKey lockKey);

        void getFRDatas(int pageNo, int pageSize, OnHttpRequestListener<LockHttpResult<FR>> listener);

        void clearFR();

        void clearFRFromServer(OnHttpRequestListener<Boolean> listener);

        void deleteFR(long FRNo);

        void deleteFRFromServer(int FRId, OnHttpRequestListener<Boolean> listener);
    }

    interface View extends IBaseView {
        int getFRID();
        void getFRDatasSuccess(LockHttpResult<FR> lockKeyHttpResult);

        void clearFRSuccess();

        void deleteFRSuccess();
    }

    interface Presenter extends IPresenter<FRManageContract.View, FRManageContract.Model> {
        void registerReceiver();

        void getFRDatas(int pageNo, int pageSize);

        void clearFR();

        void deleteFR(long FRNo);
    }
}
