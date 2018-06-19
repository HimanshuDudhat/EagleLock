package cn.jcyh.eaglelock.function.model;

import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.SendKeyContract;
import cn.jcyh.eaglelock.http.LockHttpAction;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

public class SendKeyModel implements SendKeyContract.Model {
    private LockKey mLockKey;

    @Override
    public void setLockKey(LockKey lockKey) {
        mLockKey = lockKey;
    }

    @Override
    public void sendKey(String account, long startTime, long endTime, String remarks, OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().sendKey(mLockKey.getLockId(), account, startTime, endTime, remarks, listener);
    }
}
