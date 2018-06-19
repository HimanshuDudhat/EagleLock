package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.locklib.entity.ICCard;

/**
 * Created by jogger on 2018/6/19.
 */
public interface ICManageContract {
    interface Model extends BaseModel {
        void setLockKey(LockKey lockKey);

        void getICDatas(int pageNo, int pageSize, OnHttpRequestListener<LockHttpResult<ICCard>> listener);

        void clearIC();

        void clearICFromServer(OnHttpRequestListener<Boolean> listener);

        void deleteIC(long cardNumber);

        void deleteICFromServer(int cardId, OnHttpRequestListener<Boolean> listener);
    }

    interface View extends IBaseView {
        int getCardID();
        void getICDatasSuccess(LockHttpResult<ICCard> lockKeyHttpResult);

        void clearICSuccess();

        void deleteICSuccess();
    }

    interface Presenter extends IPresenter<ICManageContract.View, ICManageContract.Model> {
        void registerReceiver();

        void getICDatas(int pageNo, int pageSize);

        void clearIC();

        void deleteIC(long cardNumber);
    }
}
