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

    void setBackPassword(String account, String pwd, int code, OnHttpRequestListener<T> listener);

    void syncDatas(String clientId, String accessToken, long lastUpdateDate, long date, OnHttpRequestListener<T> listener);

    void initLock(String clientId, LockKey lockKey, final OnHttpRequestListener<T> listener);

    void sendKey(String clientId, String accessToken, int lockId, String receiverUsername, long startDate,
                 long endDate, String remarks, long date, final OnHttpRequestListener<T> listener);

    void getPwd(String clientId, String accessToken, int lockId, int keyboardPwdVersion, int keyboardPwdType, long startTime, long endDate, long date, final OnHttpRequestListener<T> listener);

    void customPwd(String clientId, String accessToken, int lockId, String keyboardPwd, long startDate, long endDate, int addType, long date, OnHttpRequestListener<T> listener);

    void getLockKeys(String clientId, String accessToken, int lockId, int pageNo, int pageSize, long date, OnHttpRequestListener<T> listener);

    void delKey(String clientId, String accessToken, int keyId, long date, OnHttpRequestListener<T> listener);

    void resetKey(String clientId, String accessToken, int lockId, long date, OnHttpRequestListener<T> listener);

    void delAllKeys(String clientId, String accessToken, int lockId, long date, OnHttpRequestListener<T> listener);

    void getPwdsByLock(String clientId, String accessToken, int lockId, int pageNo, int pageSize, long date, OnHttpRequestListener<T> listener);

    void resetPwd(String clientId, String accessToken, int lockId, String pwdInfo, long timestamp, long date, OnHttpRequestListener<T> listener);

    void freezeKey(String clientId, String accessToken, int keyId, long date, OnHttpRequestListener<T> listener);

    void unFreezeKey(String clientId, String accessToken, int keyId, long date, OnHttpRequestListener<T> listener);

    void authKeyUser(String clientId, String accessToken, int lockId, int keyId, long date, OnHttpRequestListener<Boolean> listener);

    void unAuthKeyUser(String clientId, String accessToken, int lockId, int keyId, long date, OnHttpRequestListener<Boolean> listener);

    void lockRename(String clientId, String accessToken, int lockId, String lockAlias, long date, OnHttpRequestListener<T> listener);

    void changeAdminKeyboardPwd(String clientId, String accessToken, int lockId, String password, long date, OnHttpRequestListener<T> listener);

    void getICs(String clientId, String accessToken, int lockId, int pageNo, int pageSize, long date, OnHttpRequestListener<T> listener);

    void deleteIC(String clientId, String accessToken, int lockId, int cardId, int deleteType, long date, OnHttpRequestListener<T> listener);

    void addIC(String clientId, String accessToken, int lockId, String cardNumber, long startDate, long endDate, int addType, long date, OnHttpRequestListener<T> listener);

    void clearICs(String clientId, String accessToken, int lockId, long date, OnHttpRequestListener<T> listener);

    void getFingerprints(String clientId, String accessToken, int lockId, int pageNo, int pageSize, long date, OnHttpRequestListener<T> listener);

    void deleteFingerprint(String clientId, String accessToken, int lockId, int fingerprintId, int deleteType, long date, OnHttpRequestListener<T> listener);

    void addFingerprint(String clientId, String accessToken, int lockId, String fingerprintNumber, long startDate, long endDate, int addType, long date, OnHttpRequestListener<T> listener);

    void clearFingerprints(String clientId, String accessToken, int lockId, long date, OnHttpRequestListener<T> listener);

    void uploadLockRecords(String clientId, String accessToken, int lockId, String records, long date, OnHttpRequestListener<T> listener);

    void getLockRecords(String clientId, String accessToken, int lockId, long startDate, long endDate, int pageNo, int pageSize, long date, OnHttpRequestListener<T> listener);
}
