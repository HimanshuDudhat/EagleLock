package cn.jcyh.eaglelock.function.model;

import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.KeyInfoContract;
import cn.jcyh.eaglelock.http.LockHttpAction;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/19.
 */
public class KeyInfoModel implements KeyInfoContract.Model {
    private LockKey mLockKey;

    @Override
    public void setLockKey(LockKey lockKey) {
        mLockKey = lockKey;
    }

    @Override
    public void rename(String name, OnHttpRequestListener<Boolean> listener) {
    }

    @Override
    public void authKeyUser(OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().authKeyUser(mLockKey.getLockId(), mLockKey.getKeyId(), listener);
    }

    @Override
    public void unAuthKeyUser(OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().unAuthKeyUser(mLockKey.getLockId(), mLockKey.getKeyId(), listener);
    }

    @Override
    public void freezeKey(OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().freezeKey(mLockKey.getKeyId(), listener);
    }

    @Override
    public void unFreezeKey(OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().unFreezeKey(mLockKey.getKeyId(), listener);
    }

    @Override
    public void deleteKey(OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().delKey(mLockKey.getKeyId(), listener);
    }
}
