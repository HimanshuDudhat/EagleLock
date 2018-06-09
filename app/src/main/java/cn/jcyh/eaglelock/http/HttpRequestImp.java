package cn.jcyh.eaglelock.http;

import android.annotation.SuppressLint;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.L;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by jogger on 2018/4/14.网络请求处理
 */
@SuppressWarnings("unchecked")
class HttpRequestImp implements IHttpRequest {
    private final RequestService mRequestService;
    private Gson mGson;

    HttpRequestImp(RequestService requestService) {
        mRequestService = requestService;
        mGson = new Gson();
    }

    @Override
    public void test(String account, OnHttpRequestListener listener) {
        mRequestService.test(account);
    }

    @Override
    public void login(String account, String pwd, OnHttpRequestListener listener) {
        Observable<HttpResult<User>> login = mRequestService.login(account, pwd);
        enqueue(login, listener);
    }

    @Override
    public void regist(String account, String pwd, int code, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> regist = mRequestService.regist(account, pwd, code);
        enqueueVoid(regist, listener);
    }

    @Override
    public void sendCodeRegist(String account, String sign, long time, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> sendCodeRegist = mRequestService.sendCode(account, sign, time, 1);
        enqueueVoid(sendCodeRegist, listener);
    }

    @Override
    public void sendCodeForget(String account, String sign, long time, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> sendCodeForget = mRequestService.sendCode(account, sign, time, 2);
        enqueueVoid(sendCodeForget, listener);
    }

    /**
     * 不需要解析实体类的回调
     */
    @SuppressLint("CheckResult")
    private void enqueueVoid(Observable<Response<ResponseBody>> call, final
    OnHttpRequestListener<Boolean> listener) {
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
                        try {
                            String result = response.body().string();
                            L.e("---------enqueueVoid:" + result);
                            if (listener != null) {
                                int code = getCode(result);
                                if (code == 200) {
                                    listener.onSuccess(true);
                                } else {
                                    listener.onFailure(code);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (listener != null)
                            listener.onFailure(-1);
                    }
                });
    }


    /**
     * 自动解析
     */

    private <T> void enqueue(Observable<HttpResult<T>> call, final OnHttpRequestListener<T>
            listener) {
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult<T>>() {
                    @Override
                    public void accept(HttpResult<T> tHttpResult) throws Exception {
                        if (listener != null) {
                            if (tHttpResult.getCode() == 200)
                                listener.onSuccess(tHttpResult.getData());
                            else
                                listener.onFailure(tHttpResult.getCode());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.e("----------onError:" + throwable);
                        if (listener != null)
                            listener.onFailure(-1);
                    }
                });
    }

    private int getCode(String str) {
        int code = 0;
        try {
            JSONObject json = new JSONObject(str);
            code = json.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }


}
