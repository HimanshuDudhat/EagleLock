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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

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

    public void setExpiresin(int expiresin) {
        this.expiresin = expiresin;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", nickName='" + nickName + '\'' +
                ", access_token='" + access_token + '\'' +
                ", openid=" + openid +
                ", expiresin=" + expiresin +
                ", scope='" + scope + '\'' +
                ", refreshtoken='" + refreshtoken + '\'' +
                '}';
    }
}
