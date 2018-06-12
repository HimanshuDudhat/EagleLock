package cn.jcyh.eaglelock.http;

import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/4/14.请求接口
 */

interface IHttpRequest<T> {
    void test(String account, OnHttpRequestListener<T> listener);

    void login(String account, String pwd, OnHttpRequestListener<T> listener);

    void regist(String account, String pwd, int code, OnHttpRequestListener<T> listener);

    void sendCodeRegist(String account, String sign, long time, OnHttpRequestListener<T> listener);

    void sendCodeForget(String account, String sign, long time, OnHttpRequestListener<T> listener);

    void syncDatas(String clientId, String accessToken, long lastUpdateDate, long date, OnHttpRequestListener<T> listener);

    void initLock(String clientId, LockKey lockKey, final OnHttpRequestListener<T> listener);

}
