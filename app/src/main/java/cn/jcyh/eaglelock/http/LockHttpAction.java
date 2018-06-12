package cn.jcyh.eaglelock.http;

import cn.jcyh.eaglelock.constant.Config;
import cn.jcyh.eaglelock.control.ControlCenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/9.
 */
@SuppressWarnings("unchecked")
public class LockHttpAction extends BaseHttpAction {
    private static LockHttpAction sHttpAction;
    private static String CLIENT_ID;
    private static String CLIENT_SECRET;
    private static String ACCESS_TOKEN;

    public static LockHttpAction geHttpAction() {
        if (sHttpAction == null) {
            synchronized (LockHttpAction.class) {
                if (sHttpAction == null)
                    sHttpAction = new LockHttpAction();
            }
        }
        ACCESS_TOKEN = ControlCenter.getControlCenter().getAccessToken();
        return sHttpAction;
    }

    public void syncDatas(long lastUpdateDate, OnHttpRequestListener listener) {
        mHttpRequest.syncDatas(CLIENT_ID, ACCESS_TOKEN, lastUpdateDate, System.currentTimeMillis(), listener);
    }
    public void initLock(LockKey lockKey, final OnHttpRequestListener<Boolean> listener) {
        mHttpRequest.initLock(CLIENT_ID, lockKey, listener);
    }

    private LockHttpAction() {
        super();
        CLIENT_ID = Config.CLIENT_ID;
        CLIENT_SECRET = Config.CLIENT_SECRET;
    }

    @Override
    IHttpRequest getHttpRequest(RequestService requestService) {
        return new HttpRequestImp(requestService);
    }

    @Override
    String getBaseUrl() {
        return RequestService.LOCK_URL;
    }

}
