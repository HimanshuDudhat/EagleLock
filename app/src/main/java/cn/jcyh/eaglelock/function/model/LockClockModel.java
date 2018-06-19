package cn.jcyh.eaglelock.function.model;

import cn.jcyh.eaglelock.constant.Operation;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.LockClockContract;
import cn.jcyh.eaglelock.http.api.MyLockAPI;

public class LockClockModel implements LockClockContract.Model {
    private LockKey mLockKey;

    @Override
    public void setLockKey(LockKey lockKey) {
        mLockKey = lockKey;
    }

    @Override
    public void getLockTime() {
        if (MyLockAPI.getLockAPI().isConnected(mLockKey.getLockMac())) {
            MyLockAPI.getLockAPI().getLockTime(null, mLockKey);
        } else {
            MyLockAPI.getLockAPI().connect(mLockKey.getLockMac(), Operation.GET_LOCK_TIME);
        }
    }

    @Override
    public void setLockTime() {
        if (MyLockAPI.getLockAPI().isConnected(mLockKey.getLockMac())) {
            MyLockAPI.getLockAPI().setLockTime(null, mLockKey);
        } else {
            MyLockAPI.getLockAPI().connect(mLockKey.getLockMac(), Operation.SET_LOCK_TIME);
        }
    }
}
