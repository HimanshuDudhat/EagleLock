package cn.jcyh.eaglelock.function.model;

import android.os.Bundle;

import cn.jcyh.eaglelock.constant.Operation;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.SettingContract;
import cn.jcyh.eaglelock.http.LockHttpAction;
import cn.jcyh.eaglelock.http.api.MyLockAPI;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

public class SettingModel implements SettingContract.Model {
    private LockKey mLockKey;

    @Override
    public void setLockKey(LockKey lockKey) {
        mLockKey = lockKey;
    }

    @Override
    public void rename(String content, OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().lockRename(mLockKey.getLockId(), content, listener);
    }

    //修改管理员键盘密码/蓝牙
    @Override
    public void setAdminKeyboardPassword(String content) {
        if (MyLockAPI.getLockAPI().isConnected(mLockKey.getLockMac())) {
            MyLockAPI.getLockAPI().setAdminKeyboardPassword(null, mLockKey, content);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("password", content);
            MyLockAPI.sBleSession.setArgments(bundle);
            MyLockAPI.getLockAPI().connect(mLockKey.getLockMac(), Operation.SET_ADMIN_KEYBOARD_PASSWORD);
        }
    }

    //修改管理员键盘密码/服务器
    @Override
    public void changeAdminKeyboardPwd(String password, OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().changeAdminKeyboardPwd(mLockKey.getLockId(), password, listener);
    }

    //删除钥匙/蓝牙
    @Override
    public void deleteLockKey() {
        MyLockAPI lockAPI = MyLockAPI.getLockAPI();
        if (lockAPI.isConnected(mLockKey.getLockMac())) {
            lockAPI.resetLock(null, mLockKey.getOpenid(), mLockKey.getLockVersion(),
                    mLockKey.getAdminPwd(), mLockKey.getLockKey(), mLockKey.getLockFlagPos(), mLockKey.getAesKeystr());
        } else {
            lockAPI.connect(mLockKey.getLockMac(), Operation.RESET_LOCK);
        }
    }

    @Override
    public void deleteLockKeyFromServer(OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().delKey(mLockKey.getKeyId(), listener);
    }


}
