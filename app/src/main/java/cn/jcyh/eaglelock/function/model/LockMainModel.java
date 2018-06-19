package cn.jcyh.eaglelock.function.model;

import cn.jcyh.eaglelock.constant.Operation;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.LockMainContract;
import cn.jcyh.eaglelock.http.api.MyLockAPI;

/**
 * Created by jogger on 2018/6/13.
 */
public class LockMainModel implements LockMainContract.Model {
    private LockKey mLockKey;

    @Override
    public void setLockKey(LockKey lockKey) {
        mLockKey = lockKey;
    }

    @Override
    public void unlock() {
        if (MyLockAPI.getLockAPI().isConnected(mLockKey.getLockKey())) {
            if (mLockKey.isAdmin())
                MyLockAPI.getLockAPI().unlockByAdministrator(null, mLockKey);
            else
                MyLockAPI.getLockAPI().unlockByUser(null, mLockKey);
        } else {
            MyLockAPI.getLockAPI().connect(mLockKey.getLockMac(), Operation.LOCKCAR_DOWN);
        }
    }

}
