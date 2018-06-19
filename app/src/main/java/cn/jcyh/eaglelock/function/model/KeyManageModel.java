package cn.jcyh.eaglelock.function.model;

import cn.jcyh.eaglelock.constant.Operation;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.KeyManageContract;
import cn.jcyh.eaglelock.http.LockHttpAction;
import cn.jcyh.eaglelock.http.api.MyLockAPI;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/15.
 */
public class KeyManageModel implements KeyManageContract.Model {
    private LockKey mLockKey;

    @Override
    public void setLockKey(LockKey lockKey) {
        mLockKey = lockKey;
    }

    @Override
    public void getLockKeys(int pageNo, int pageSize, OnHttpRequestListener<LockHttpResult<LockKey>> listener) {
        LockHttpAction.getHttpAction().getLockKeys(mLockKey.getLockId(), pageNo, pageSize, listener);
    }

    @Override
    public void resetKey() {
        MyLockAPI lockAPI = MyLockAPI.getLockAPI();
        if (lockAPI.isConnected(mLockKey.getLockMac()))
            lockAPI.resetEKey(null, mLockKey);
        else {
            lockAPI.connect(mLockKey.getLockMac(), Operation.RESET_EKEY);
        }
    }

    @Override
    public void resetKeyFromServer(OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().resetKey(mLockKey.getLockId(), listener);
    }

    @Override
    public void clearKey(OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().delAllKeys(mLockKey.getLockId(), listener);
    }
}
