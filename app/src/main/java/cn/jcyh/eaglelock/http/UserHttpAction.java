package cn.jcyh.eaglelock.http;

import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/9.
 */
@SuppressWarnings("unchecked")
public class UserHttpAction extends BaseHttpAction {
    private static UserHttpAction sHttpAction;

    public static UserHttpAction geHttpAction() {
        if (sHttpAction == null) {
            synchronized (UserHttpAction.class) {
                if (sHttpAction == null)
                    sHttpAction = new UserHttpAction();
            }
        }
        return sHttpAction;
    }

    private UserHttpAction() {
        super();
    }

    @Override
    public IHttpRequest getHttpRequest(RequestService requestService) {
        return new HttpRequestImp(requestService);
    }

    @Override
    public String getBaseUrl() {
        return RequestService.BASE_URL;
    }

    public void login(String account, String password, final OnHttpRequestListener<HttpResult<User>> listener) {
        mHttpRequest.login(account, password, listener);
    }

    public void regist(String account, String pwd, int code, OnHttpRequestListener<Boolean> listener) {
        mHttpRequest.regist(account, pwd, code, listener);
    }

    public void sendCodeRegist(String account, String sign, long time, OnHttpRequestListener<Boolean> listener) {
        mHttpRequest.sendCodeRegist(account, sign, time, listener);
    }

    public void sendCodeForget(String account, String sign, long time, OnHttpRequestListener<Boolean> listener) {
        mHttpRequest.sendCodeForget(account, sign, time, listener);
    }

}
