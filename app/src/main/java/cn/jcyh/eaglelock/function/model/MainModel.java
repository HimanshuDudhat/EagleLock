package cn.jcyh.eaglelock.function.model;

import cn.jcyh.eaglelock.function.contract.MainContract;
import cn.jcyh.eaglelock.http.LockHttpAction;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/12.
 */
public class MainModel implements MainContract.Model {
    @Override
    public void syncDatas(long lastUpdateDate, OnHttpRequestListener listener) {
        LockHttpAction.getHttpAction().syncDatas(lastUpdateDate, listener);
    }
}
