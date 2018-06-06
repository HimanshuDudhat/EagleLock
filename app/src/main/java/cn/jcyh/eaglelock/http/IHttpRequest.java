package cn.jcyh.eaglelock.http;

import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/4/14.请求接口
 */

interface IHttpRequest<T> {
    void test(String account, String sign, long time, int type, final OnHttpRequestListener<T> listener);


}
