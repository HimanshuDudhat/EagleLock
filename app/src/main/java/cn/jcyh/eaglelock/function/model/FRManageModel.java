package cn.jcyh.eaglelock.function.model;

import android.os.Bundle;

import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.constant.Operation;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.FRManageContract;
import cn.jcyh.eaglelock.http.LockHttpAction;
import cn.jcyh.eaglelock.http.api.MyLockAPI;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.locklib.entity.FR;

/**
 * Created by jogger on 2018/6/19.
 */
public class FRManageModel implements FRManageContract.Model {
    private LockKey mLockKey;

    @Override
    public void setLockKey(LockKey lockKey) {
        mLockKey = lockKey;
    }

    @Override
    public void getFRDatas(int pageNo, int pageSize, OnHttpRequestListener<LockHttpResult<FR>> listener) {
        LockHttpAction.getHttpAction().getFingerprints(mLockKey.getLockId(), pageNo, pageSize, listener);
    }

    @Override
    public void clearFR() {
        if (MyLockAPI.getLockAPI().isConnected(mLockKey.getLockMac()))
            MyLockAPI.getLockAPI().clearFingerPrint(null, mLockKey);
        else {
            MyLockAPI.getLockAPI().connect(mLockKey.getLockMac(), Operation.CLEAR_FINGERPRINTS);
        }
    }

    @Override
    public void clearFRFromServer(OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().clearFingerprints(mLockKey.getLockId(), listener);
    }

    @Override
    public void deleteFR(long FRNo) {
        if (MyLockAPI.getLockAPI().isConnected(mLockKey.getLockMac())) {
            MyLockAPI.getLockAPI().deleteFingerPrint(null,FRNo, mLockKey);
        } else {
            Bundle bundle = new Bundle();
            bundle.putLong(Constant.FRNO,FRNo);
            MyLockAPI.sBleSession.setArgments(bundle);
            MyLockAPI.getLockAPI().connect(mLockKey.getLockMac(), Operation.DELETE_FINGERPRINT);
        }
    }

    @Override
    public void deleteFRFromServer(int FRNo,OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().deleteFingerprint(mLockKey.getLockId(),FRNo,listener);
    }
}
