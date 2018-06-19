package cn.jcyh.eaglelock.http;

import android.annotation.SuppressLint;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.entity.LockKeyboardPwd;
import cn.jcyh.eaglelock.entity.LockPwdRecord;
import cn.jcyh.eaglelock.entity.SyncData;
import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.http.bean.HttpResult;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.GsonUtil;
import cn.jcyh.eaglelock.util.L;
import cn.jcyh.locklib.entity.FR;
import cn.jcyh.locklib.entity.LockRecord;
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
@SuppressLint("CheckResult")
class HttpRequestImp implements IHttpRequest {
    private final RequestService mRequestService;

    HttpRequestImp(RequestService requestService) {
        mRequestService = requestService;
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

    @Override
    public void setBackPassword(String account, String pwd, int code, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> setBackPassword = mRequestService.setBackPassword(account, pwd, code);
        enqueueVoid(setBackPassword, listener);
    }

    @Override
    public void syncDatas(String clientId, String accessToken, long lastUpdateDate, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> syncDatas = mRequestService.syncData(clientId, accessToken, lastUpdateDate, date);
        lockEnqueue(syncDatas, SyncData.class, listener);
    }

    @Override
    public void initLock(String clientId, LockKey lockKey, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> initLock = mRequestService.initLock(
                clientId,
                lockKey.getAccessToken(),
                lockKey.getLockName(),
                lockKey.getLockAlias(),
                lockKey.getLockMac(),
                lockKey.getLockKey(),
                lockKey.getLockFlagPos(),
                lockKey.getAesKeystr(),
                lockKey.getLockVersion(),
                lockKey.getAdminPwd(),
                lockKey.getNoKeyPwd(),
                lockKey.getDeletePwd(),
                lockKey.getPwdInfo(),
                lockKey.getTimestamp(),
                lockKey.getSpecialValue(),
                lockKey.getTimezoneRawOffset(),
                lockKey.getModelNumber(),
                lockKey.getHardwareRevision(),
                lockKey.getFirmwareRevision(),
                System.currentTimeMillis());
        lockEnqueueVoid(initLock, listener);
    }

    @Override
    public void sendKey(String clientId, String accessToken, int lockId, String receiverUsername, long startDate, long endDate, String remarks, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> sendKey = mRequestService.sendKey(clientId, accessToken, lockId, receiverUsername, startDate, endDate, remarks, date);
        lockEnqueueVoid(sendKey, listener);
    }

    @Override
    public void getPwd(String clientId, String accessToken, int lockId, int keyboardPwdVersion, int keyboardPwdType, long startTime, long endDate, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> getPwd = mRequestService.getPwd(clientId, accessToken, lockId, keyboardPwdVersion, keyboardPwdType, startTime, endDate, date);
        lockEnqueue(getPwd, LockKeyboardPwd.class, listener);
    }

    @Override
    public void customPwd(String clientId, String accessToken, int lockId, String keyboardPwd, long startDate, long endDate, int addType, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> customPwd =  mRequestService.customPwd(clientId, accessToken, lockId, keyboardPwd, startDate, endDate, addType, date);
        lockEnqueue(customPwd, LockKeyboardPwd.class, listener);
    }

    @Override
    public void getLockKeys(String clientId, String accessToken, int lockId, int pageNo, int pageSize, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> getLockKeys = mRequestService.getLockKeys(clientId, accessToken, lockId, pageNo, pageSize, System.currentTimeMillis());
        lockEnqueueList(getLockKeys, LockKey.class, listener);
    }

    @Override
    public void delKey(String clientId, String accessToken, int keyId, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> delKey = mRequestService.delKey(clientId, accessToken, keyId, date);
        lockEnqueueVoid(delKey, listener);
    }

    @Override
    public void resetKey(String clientId, String accessToken, int keyId, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> resetKey = mRequestService.resetKey(clientId, accessToken, keyId, date);
        lockEnqueueVoid(resetKey, listener);
    }

    @Override
    public void delAllKeys(String clientId, String accessToken, int lockId, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> delAllKeys = mRequestService.delAllKeys(clientId, accessToken, lockId, date);
        lockEnqueueVoid(delAllKeys, listener);
    }

    @Override
    public void getPwdsByLock(String clientId, String accessToken, int lockId, int pageNo, int pageSize, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> getPwdsByLock = mRequestService.getPwdsByLock(clientId, accessToken, lockId, pageNo, pageSize, date);
        lockEnqueueList(getPwdsByLock, LockPwdRecord.class, listener);
    }

    @Override
    public void resetPwd(String clientId, String accessToken, int lockId, String pwdInfo, long timestamp, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> resetPwd = mRequestService.resetKeyboardPwd(clientId, accessToken, lockId, pwdInfo, timestamp, date);
        lockEnqueueVoid(resetPwd, listener);
    }

    @Override
    public void freezeKey(String clientId, String accessToken, int keyId, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> freezeKey = mRequestService.freezeKey(clientId, accessToken, keyId, date);
        lockEnqueueVoid(freezeKey, listener);
    }

    @Override
    public void unFreezeKey(String clientId, String accessToken, int keyId, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> freezeKey = mRequestService.unFreezeKey(clientId, accessToken, keyId, date);
        lockEnqueueVoid(freezeKey, listener);
    }

    @Override
    public void lockRename(String clientId, String accessToken, int lockId, String lockAlias, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> lockRename = mRequestService.lockRename(clientId, accessToken, lockId, lockAlias, date);
        lockEnqueueVoid(lockRename, listener);
    }

    @Override
    public void changeAdminKeyboardPwd(String clientId, String accessToken, int lockId, String password, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> changeAdminKeyboardPwd = mRequestService.changeAdminKeyboardPwd(clientId, accessToken, lockId, password, date);
        lockEnqueueVoid(changeAdminKeyboardPwd, listener);
    }

    @Override
    public void getICs(String clientId, String accessToken, int lockId, int pageNo, int pageSize, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> getICs = mRequestService.getICs(clientId, accessToken, lockId, pageNo, pageSize, date);
        lockEnqueueList(getICs, ICCard.class, listener);
    }

    @Override
    public void deleteIC(String clientId, String accessToken, int lockId, int cardId, int deleteType, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> deleteIC = mRequestService.deleteIC(clientId, accessToken, lockId, cardId, deleteType, date);
        lockEnqueueVoid(deleteIC, listener);
    }

    @Override
    public void addIC(String clientId, String accessToken, int lockId, String cardNumber, long startDate, long endDate, int addType, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> addIC = mRequestService.addIC(clientId, accessToken, lockId, cardNumber, startDate, endDate, addType, date);
        lockEnqueueVoid(addIC, listener);
    }

    @Override
    public void clearICs(String clientId, String accessToken, int lockId, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> clearICs = mRequestService.clearICs(clientId, accessToken, lockId, date);
        lockEnqueueVoid(clearICs, listener);
    }

    @Override
    public void getFingerprints(String clientId, String accessToken, int lockId, int pageNo, int pageSize, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> getFingerprints = mRequestService.getFingerprints(clientId, accessToken, lockId, pageNo, pageSize, date);
        lockEnqueueList(getFingerprints, FR.class, listener);
    }

    @Override
    public void deleteFingerprint(String clientId, String accessToken, int lockId, int fingerprintId, int deleteType, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> deleteFingerprint = mRequestService.deleteFingerprint(clientId, accessToken, lockId, fingerprintId, deleteType, date);
        lockEnqueueVoid(deleteFingerprint, listener);
    }

    @Override
    public void addFingerprint(String clientId, String accessToken, int lockId, String fingerprintNumber, long startDate, long endDate, int addType, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> addFingerprint = mRequestService.addFingerprint(clientId, accessToken, lockId, fingerprintNumber, startDate, endDate, addType, date);
        lockEnqueueVoid(addFingerprint, listener);
    }

    @Override
    public void clearFingerprints(String clientId, String accessToken, int lockId, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> clearFingerprints = mRequestService.clearFingerprints(clientId, accessToken, lockId, date);
        lockEnqueueVoid(clearFingerprints, listener);
    }

    @Override
    public void uploadLockRecords(String clientId, String accessToken, int lockId, String records, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> uploadLockRecords = mRequestService.uploadLockRecords(clientId, accessToken, lockId, records, date);
        lockEnqueueVoid(uploadLockRecords, listener);
    }

    @Override
    public void getLockRecords(String clientId, String accessToken, int lockId, long startDate, long endDate, int pageNo, int pageSize, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> getLockRecords = mRequestService.getLockRecords(clientId, accessToken, lockId, startDate, endDate, pageNo, pageSize, date);
        lockEnqueueList(getLockRecords, LockRecord.class, listener);
    }

    @Override
    public void unAuthKeyUser(String clientId, String accessToken, int lockId, int keyId, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> unAuthKeyUser = mRequestService.unAuthKeyUser(clientId, accessToken, lockId, keyId, date);
        lockEnqueueVoid(unAuthKeyUser, listener);
    }

    @Override
    public void authKeyUser(String clientId, String accessToken, int lockId, int keyId, long date, OnHttpRequestListener listener) {
        Observable<Response<ResponseBody>> authKeyUser = mRequestService.authKeyUser(clientId, accessToken, lockId, keyId, date);
        lockEnqueueVoid(authKeyUser, listener);
    }

    /**
     * 不需要解析实体类的回调
     */
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

    /*-----------------锁相关请求结果不一样-------------------*/
    //获取列表
    private <T> void lockEnqueueList(final Observable<Response<ResponseBody>> call, final Class<T> clazz, final OnHttpRequestListener<T>
            listener) {
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
                        String result = response.body().string();
                        L.e("---------result:" + result);
                        if (listener != null) {
                            HttpError httpError = GsonUtil.fromJson(result, HttpError.class);
                            if (httpError.getErrcode() != 0) {
                                listener.onFailure(httpError.getErrcode());
                            } else {
                                LockHttpResult httpResult = new LockHttpResult();
//                                HttpResult httpResult = mGson.fromJson(result, HttpResult.class);
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray list_array = jsonObject.getJSONArray("list");

                                ArrayList<T> list = new ArrayList<>();
                                JsonArray array = new JsonParser().parse(list_array.toString()).getAsJsonArray();
                                for (final JsonElement elem : array) {
                                    list.add(GsonUtil.fromJson(elem, clazz));
                                }
                                httpResult.setList(list);
                                listener.onSuccess((T) httpResult);
                            }
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

    private <T> void lockEnqueue(final Observable<Response<ResponseBody>> call, final Class<T> clazz, final OnHttpRequestListener<T>
            listener) {
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
                        String result = response.body().string();
                        L.e("---------result:" + result);
                        if (listener != null) {
                            HttpError httpError = GsonUtil.fromJson(result, HttpError.class);
                            if (httpError.getErrcode() != 0) {
                                listener.onFailure(httpError.getErrcode());
                            } else {
                                listener.onSuccess(GsonUtil.fromJson(result, clazz));
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.e("-------onError:" + throwable + mRequestService);
                        if (listener != null)
                            listener.onFailure(-1);
                    }
                });
    }

    private void lockEnqueueVoid(final Observable<Response<ResponseBody>> call, final OnHttpRequestListener<Boolean>
            listener) {
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> response) throws Exception {
                        String result = response.body().string();
                        L.e("------------result:" + result);
                        if (listener != null) {
                            HttpError httpError = GsonUtil.fromJson(result, HttpError.class);
                            if (httpError.getErrcode() != 0) {
                                listener.onFailure(httpError.getErrcode());
                            } else {
                                listener.onSuccess(true);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.e("-----------onError:" + throwable);
                        if (listener != null)
                            listener.onFailure(-1);
                    }
                });
    }

}
