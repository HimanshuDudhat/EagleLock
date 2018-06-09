package cn.jcyh.eaglelock.http;

import cn.jcyh.eaglelock.entity.User;
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
    String LOCK_URL = "";

    //发送验证码
    Observable<Response<ResponseBody>> test(@Field("userPage.cellphone") String account);

    @FormUrlEncoded
    @POST("Locks/SendCode")
        //发送验证码
    Observable<Response<ResponseBody>> sendCode(@Field("account") String account,
                                                @Field("sign") String sign,
                                                @Field("time") long time,
                                                @Field("type") int type);

    @FormUrlEncoded
    @POST("Locks/Register")
    Observable<Response<ResponseBody>> regist(@Field("account") String account, @Field
            ("pwd") String pwd, @Field("code") int code);

    //        @FormUrlEncoded
//    @POST("App/Login")
//    Observable<Response<ResponseBody>> login(@Field("account") String account, @Field
//            ("pwd") String pwd);
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
    Observable<Response<ResponseBody>> sendNewPwd(@Field("account") String account, @Field
            ("pwd") String pwd, @Field("code") String code);
}
