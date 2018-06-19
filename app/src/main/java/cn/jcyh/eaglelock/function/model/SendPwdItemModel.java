package cn.jcyh.eaglelock.function.model;

import android.os.Bundle;

import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.constant.Operation;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.entity.LockKeyboardPwd;
import cn.jcyh.eaglelock.function.contract.SendPwdItemContract;
import cn.jcyh.eaglelock.http.LockHttpAction;
import cn.jcyh.eaglelock.http.api.MyLockAPI;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/14.
 */
public class SendPwdItemModel implements SendPwdItemContract.Model {
    private LockKey mLockKey;

    @Override
    public void setLockKey(LockKey lockKey) {
        mLockKey = lockKey;
    }

    //自定义密码
    @Override
    public void customPwd(String keyboardPwd, long startTime, long endTime, OnHttpRequestListener<LockKeyboardPwd> listener) {
        LockHttpAction.getHttpAction().customPwd(mLockKey.getLockId(), keyboardPwd, startTime, endTime, listener);
    }

    //从锁中获取密码
    @Override
    public void addPeriodKeyboardPassword(String keyboardPwd, long startTime, long endTime) {
        mLockKey.setStartDate(startTime);
        mLockKey.setEndDate(endTime);
        if (MyLockAPI.getLockAPI().isConnected(mLockKey.getLockMac()))
            MyLockAPI.getLockAPI().addPeriodKeyboardPassword(null,
                    keyboardPwd,
                    mLockKey
            );
        else {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.PWD, keyboardPwd);
            bundle.putLong(Constant.START_DATE, startTime);
            bundle.putLong(Constant.END_DATE, endTime);
            MyLockAPI.getLockAPI().connect(mLockKey.getLockMac(), bundle, Operation.CUSTOM_PWD);
        }
    }

    @Override
    public void getPwd(int keyboardPwdType, long startTime, long endTime, OnHttpRequestListener<LockKeyboardPwd> listener) {
        LockHttpAction.getHttpAction().getPwd(mLockKey.getLockId(), keyboardPwdType, startTime, endTime, listener);
    }
}
