package cn.jcyh.eaglelock.function.contract;

import java.util.List;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/12.
 */
public interface MainContract {
    interface Model extends BaseModel {
        void syncDatas(long lastUpdateDate, OnHttpRequestListener listener);
    }

    interface View extends IBaseView {
        void syncDataSuccess(List<LockKey> keyList);
    }

    interface Presenter extends IPresenter<View, Model> {
        void syncDatas();
    }
}
