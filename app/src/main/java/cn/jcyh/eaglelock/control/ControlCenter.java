package cn.jcyh.eaglelock.control;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.util.SPUtil;

/**
 * Created by jogger on 2018/5/2.
 */

public class ControlCenter {
    private static ControlCenter sControlCenter;
    private Gson mGson;
    public static LockKey sCurrentKey;//记录当前操作的key
    public static int sOpenID = -1;
    public static User sUser;
    public static List<LockKey> sUserKeys;
    private final SPUtil mSPUtil;

    private ControlCenter() {
        mGson = new Gson();
        mSPUtil = SPUtil.getInstance();
    }

    public static ControlCenter getControlCenter() {
        if (sControlCenter == null) {
            synchronized (ControlCenter.class) {
                if (sControlCenter == null)
                    sControlCenter = new ControlCenter();
            }
        }
        return sControlCenter;
    }

    public void saveIsAutoLogin(boolean autoLogin) {
        mSPUtil.put(Constant.AUTO_LOGIN, autoLogin);
    }

    public boolean getIsAutoLogin() {
        return mSPUtil.getBoolean(Constant.AUTO_LOGIN, false);
    }

    public void saveUserAccount(String account) {
        mSPUtil.put(Constant.ACCOUNT, account);
    }

    public void saveUserPwd(String pwd) {
        mSPUtil.put(Constant.PWD, pwd);
    }

    public String getAccount() {
        return mSPUtil.getString(Constant.ACCOUNT, "");
    }

    public String getPwd() {
        return mSPUtil.getString(Constant.PWD, "");
    }

    public String getAccessToken() {
        User userInfo = getUserInfo();
        if (userInfo != null)
            return userInfo.getAccess_token();
        return null;
    }

    public int getOpenID() {
        if (sOpenID == -1) {
            User userInfo = getUserInfo();
            if (userInfo != null)
                sOpenID = userInfo.getOpenid();
        }
        return sOpenID;
    }

    public void saveUserInfo(User user) {
        sUser = user;
        mSPUtil.put(Constant.USER_INFO, mGson.toJson(user));
    }

    public User getUserInfo() {
        if (sUser == null)
            sUser = mGson.fromJson(mSPUtil.getString(Constant.USER_INFO, ""), User.class);
        return sUser;
    }

    public void saveLockKeys(List<LockKey> keys) {
        sUserKeys = keys;
        mSPUtil.put(Constant.KEY_LIST, keys == null ? "" : mGson.toJson(keys));
    }

    public List<LockKey> getLockKeys() {
        if (sUserKeys == null || sUserKeys.size() == 0)
            sUserKeys = mGson.fromJson(mSPUtil.getString(Constant.KEY_LIST, ""), new TypeToken<List<LockKey>>() {
            }.getType());
        return sUserKeys;
    }

    public void saveLastSyncDate(long time) {
        mSPUtil.put(Constant.LAST_SYNC_DATE, time);
    }

    public long getLastSyncDate() {
        return mSPUtil.getLong(Constant.LAST_SYNC_DATE, 0);
    }

}
