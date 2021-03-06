package cn.jcyh.eaglelock.http;

import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.http.bean.HttpResult;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jogger on 2018/1/10.
 */

public interface RequestService {
    String BASE_URL = "http://119.23.58.28:8083/";
    String LOCK_URL = "https://api.sciener.cn/";

    Observable<Response<ResponseBody>> test(@Field("userPage.cellphone") String account);

    //发送验证码
    @FormUrlEncoded
    @POST("Locks/SendCode")
    Observable<Response<ResponseBody>> sendCode(@Field("account") String account,
                                                @Field("sign") String sign,
                                                @Field("time") long time,
                                                @Field("type") int type);

    @FormUrlEncoded
    @POST("Locks/Register")
    Observable<Response<ResponseBody>> regist(@Field("account") String account, @Field
            ("pwd") String pwd, @Field("code") int code);

    @FormUrlEncoded
    @POST("Locks/Login")
    Observable<HttpResult<User>> login(@Field("account") String account, @Field
            ("pwd") String pwd);

    @FormUrlEncoded
    @POST("Locks/Logout")
    Observable<Response<ResponseBody>> logout(@Field("userId") long userId);

    //设置新密码（忘记密码）
    @FormUrlEncoded
    @POST("Locks/SetBackPass")
    Observable<Response<ResponseBody>> setBackPassword(@Field("account") String account, @Field
            ("pwd") String pwd, @Field("code") int code);

    /**
     * 同步钥匙数据 APP第一次同步数据，不需要传lastUpdateDate，服务端会返回全量的钥匙数据。
     *
     * @param clientId       注册时分配的app_id
     * @param accessToken    访问令牌
     * @param lastUpdateDate 最近同步时间(最后一次调用该接口，服务端返回的)，不传则返回全量的钥匙数据。
     * @param date           当前时间（毫秒数）
     */
    @FormUrlEncoded
    @POST("/v3/key/syncData")
    Observable<Response<ResponseBody>> syncData(@Field("clientId") String clientId,
                                                @Field("accessToken") String accessToken,
                                                @Field("lastUpdateDate") long lastUpdateDate,
                                                @Field("date") long date);

    /**
     * 锁初始化接口将锁相关的数据在服务端做初始化，同时会为调用该接口的用户生成一把管理员钥匙。
     * 锁初始化成功后，管理员钥匙拥有者就可以给其他用户发送普通钥匙或是发送密码了。
     */
    @FormUrlEncoded
    @POST("/v3/lock/init")
    Observable<Response<ResponseBody>> initLock(@Field("clientId") String clientId,
                                                @Field("accessToken") String accessToken,
                                                @Field("lockName") String lockName,
                                                @Field("lockAlias") String lockAlias,
                                                @Field("lockMac") String lockMac,
                                                @Field("lockKey") String lockKey,
                                                @Field("lockFlagPos") int lockFlagPos,
                                                @Field("aesKeyStr") String aesKeyStr,
                                                @Field("lockVersion") String lockVersion,
                                                @Field("adminPwd") String adminPwd,
                                                @Field("noKeyPwd") String noKeyPwd,
                                                @Field("deletePwd") String deletePwd,
                                                @Field("pwdInfo") String pwdInfo,
                                                @Field("timestamp") long timestamp,
                                                @Field("specialValue") int specialValue,
                                                @Field("timezoneRawOffset") int timezoneRawOffset,
                                                @Field("modelNum") String modelNum,
                                                @Field("hardwareRevision") String hardwareRevision,
                                                @Field("firmwareRevision") String firmwareRevision,
                                                @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/key/send")
    Observable<Response<ResponseBody>> sendKey(@Field("clientId") String clientId,
                                               @Field("accessToken") String accessToken,
                                               @Field("lockId") int lockId,
                                               @Field("receiverUsername") String receiverUsername,
                                               @Field("startDate") long startDate,
                                               @Field("endDate") long endDate,
                                               @Field("remarks") String remarks,
                                               @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/keyboardPwd/get")
    Observable<Response<ResponseBody>> getPwd(@Field("clientId") String clientId,
                                              @Field("accessToken") String accessToken,
                                              @Field("lockId") int lockId,
                                              @Field("keyboardPwdVersion") int keyboardPwdVersion,
                                              @Field("keyboardPwdType") int keyboardPwdType,
                                              @Field("startDate") long startDate,
                                              @Field("endDate") long endDate,
                                              @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/keyboardPwd/add")
    Observable<Response<ResponseBody>> customPwd(@Field("clientId") String clientId,
                                                 @Field("accessToken") String accessToken,
                                                 @Field("lockId") int lockId,
                                                 @Field("keyboardPwd") String keyboardPwd,
                                                 @Field("startDate") long startDate,
                                                 @Field("endDate") long endDate,
                                                 @Field("addType") int addType,
                                                 @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/lock/listKey")
    Observable<Response<ResponseBody>> getLockKeys(@Field("clientId") String clientId,
                                                   @Field("accessToken") String accessToken,
                                                   @Field("lockId") int lockId,
                                                   @Field("pageNo") int pageNo,
                                                   @Field("pageSize") int pageSize,
                                                   @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/key/delete")
    Observable<Response<ResponseBody>> delKey(@Field("clientId") String clientId,
                                              @Field("accessToken") String accessToken,
                                              @Field("keyId") int keyId,
                                              @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/lock/resetKey")
    Observable<Response<ResponseBody>> resetKey(@Field("clientId") String clientId,
                                                @Field("accessToken") String accessToken,
                                                @Field("lockId") int keyId,
                                                @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/lock/deleteAllKey")
    Observable<Response<ResponseBody>> delAllKeys(@Field("clientId") String clientId,
                                                  @Field("accessToken") String accessToken,
                                                  @Field("lockId") int lockId,
                                                  @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/lock/listKeyboardPwd")
    Observable<Response<ResponseBody>> getPwdsByLock(@Field("clientId") String clientId,
                                                     @Field("accessToken") String accessToken,
                                                     @Field("lockId") int lockId,
                                                     @Field("pageNo") int pageNo,
                                                     @Field("pageSize") int pageSize,
                                                     @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/lock/resetKeyboardPwd")
    Observable<Response<ResponseBody>> resetKeyboardPwd(@Field("clientId") String clientId,
                                                        @Field("accessToken") String accessToken,
                                                        @Field("lockId") int lockId,
                                                        @Field("pwdInfo") String pwdInfo,
                                                        @Field("timestamp") long timestamp,
                                                        @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/key/freeze")
    Observable<Response<ResponseBody>> freezeKey(@Field("clientId") String clientId,
                                                 @Field("accessToken") String accessToken,
                                                 @Field("keyId") int keyId,
                                                 @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/key/unfreeze")
    Observable<Response<ResponseBody>> unFreezeKey(@Field("clientId") String clientId,
                                                   @Field("accessToken") String accessToken,
                                                   @Field("keyId") int keyId,
                                                   @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/key/authorize")
    Observable<Response<ResponseBody>> authKeyUser(@Field("clientId") String clientId,
                                                   @Field("accessToken") String accessToken,
                                                   @Field("lockId") int lockId,
                                                   @Field("keyId") int keyId,
                                                   @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/key/unauthorize")
    Observable<Response<ResponseBody>> unAuthKeyUser(@Field("clientId") String clientId,
                                                     @Field("accessToken") String accessToken,
                                                     @Field("lockId") int lockId,
                                                     @Field("keyId") int keyId,
                                                     @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/lock/rename")
    Observable<Response<ResponseBody>> lockRename(@Field("clientId") String clientId,
                                                  @Field("accessToken") String accessToken,
                                                  @Field("lockId") int lockId,
                                                  @Field("lockAlias") String lockAlias,
                                                  @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/lock/changeAdminKeyboardPwd")
    Observable<Response<ResponseBody>> changeAdminKeyboardPwd(@Field("clientId") String clientId,
                                                              @Field("accessToken") String accessToken,
                                                              @Field("lockId") int lockId,
                                                              @Field("password") String password,
                                                              @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/identityCard/list")
    Observable<Response<ResponseBody>> getICs(@Field("clientId") String clientId,
                                              @Field("accessToken") String accessToken,
                                              @Field("lockId") int lockId,
                                              @Field("pageNo") int pageNo,
                                              @Field("pageSize") int pageSize,
                                              @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/identityCard/delete")
    Observable<Response<ResponseBody>> deleteIC(@Field("clientId") String clientId,
                                                @Field("accessToken") String accessToken,
                                                @Field("lockId") int lockId,
                                                @Field("cardId") int cardId,
                                                @Field("deleteType") int deleteType,
                                                @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/identityCard/add")
    Observable<Response<ResponseBody>> addIC(@Field("clientId") String clientId,
                                             @Field("accessToken") String accessToken,
                                             @Field("lockId") int lockId,
                                             @Field("cardNumber") String cardNumber,
                                             @Field("startDate") long startDate,
                                             @Field("endDate") long endDate,
                                             @Field("addType") int addType,
                                             @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/identityCard/clear ")
    Observable<Response<ResponseBody>> clearICs(@Field("clientId") String clientId,
                                                @Field("accessToken") String accessToken,
                                                @Field("lockId") int lockId,
                                                @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/fingerprint/list")
    Observable<Response<ResponseBody>> getFingerprints(@Field("clientId") String clientId,
                                                       @Field("accessToken") String accessToken,
                                                       @Field("lockId") int lockId,
                                                       @Field("pageNo") int pageNo,
                                                       @Field("pageSize") int pageSize,
                                                       @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/fingerprint/delete")
    Observable<Response<ResponseBody>> deleteFingerprint(@Field("clientId") String clientId,
                                                         @Field("accessToken") String accessToken,
                                                         @Field("lockId") int lockId,
                                                         @Field("fingerprintId") int fingerprintId,
                                                         @Field("deleteType") int deleteType,
                                                         @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/fingerprint/add")
    Observable<Response<ResponseBody>> addFingerprint(@Field("clientId") String clientId,
                                                      @Field("accessToken") String accessToken,
                                                      @Field("lockId") int lockId,
                                                      @Field("fingerprintNumber") String fingerprintNumber,
                                                      @Field("startDate") long startDate,
                                                      @Field("endDate") long endDate,
                                                      @Field("addType") int addType,
                                                      @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/fingerprint/clear")
    Observable<Response<ResponseBody>> clearFingerprints(@Field("clientId") String clientId,
                                                         @Field("accessToken") String accessToken,
                                                         @Field("lockId") int lockId,
                                                         @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/lockRecord/upload")
    Observable<Response<ResponseBody>> uploadLockRecords(@Field("clientId") String clientId,
                                                         @Field("accessToken") String accessToken,
                                                         @Field("lockId") int lockId,
                                                         @Field("records") String records,
                                                         @Field("date") long date);

    @FormUrlEncoded
    @POST("/v3/lockRecord/list")
    Observable<Response<ResponseBody>> getLockRecords(@Field("clientId") String clientId,
                                                      @Field("accessToken") String accessToken,
                                                      @Field("lockId") int lockId,
                                                      @Field("startDate") long startDate,
                                                      @Field("endDate") long endDate,
                                                      @Field("pageNo") int pageNo,
                                                      @Field("pageSize") int pageSize,
                                                      @Field("date") long date);
}
