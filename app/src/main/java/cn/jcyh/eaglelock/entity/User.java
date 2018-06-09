package cn.jcyh.eaglelock.entity;

/**
 * Created by jogger on 2018/4/26.
 */

public class User {
    private long id;
    private String account;
    private String nickName;
    private String access_token;
    private int openid;
    private int expiresin;
    private String scope;
    private String refreshtoken;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getOpenid() {
        return openid;
    }

    public void setOpenid(int openid) {
        this.openid = openid;
    }

    public int getExpiresin() {
        return expiresin;
    }

    public void setExpires_in(int expires_in) {
        this.expiresin = expiresin;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefresh_token() {
        return refreshtoken;
    }

    public void setRefresh_token(String refresh_token) {
        this.refreshtoken = refresh_token;
    }

    @Override
    public String toString() {
        return "User{" +
                "access_token='" + access_token + '\'' +
                ", openid=" + openid +
                ", expiresin=" + expiresin +
                ", scope='" + scope + '\'' +
                ", refreshtoken='" + refreshtoken + '\'' +
                '}';
    }
}
