package cn.jcyh.eaglelock.function.model;

import android.os.Bundle;

import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.constant.Operation;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.ICManageContract;
import cn.jcyh.eaglelock.http.LockHttpAction;
import cn.jcyh.eaglelock.http.api.MyLockAPI;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.locklib.entity.ICCard;

/**
 * Created by jogger on 2018/6/19.
 */
public class ICManageModel implements ICManageContract.Model {
    private LockKey mLockKey;

    @Override
    public void setLockKey(LockKey lockKey) {
        mLockKey = lockKey;
    }

    @Override
    public void getICDatas(int pageNo, int pageSize, OnHttpRequestListener<LockHttpResult<ICCard>> listener) {
        LockHttpAction.getHttpAction().getICs(mLockKey.getLockId(), pageNo, pageSize, listener);
    }

    @Override
    public void clearIC() {
        if (MyLockAPI.getLockAPI().isConnected(mLockKey.getLockMac()))
            MyLockAPI.getLockAPI().clearICCard(null, mLockKey);
        else {
            MyLockAPI.getLockAPI().connect(mLockKey.getLockMac(), Operation.CLEAR_IC_CARD);
        }
    }

    @Override
    public void clearICFromServer(OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().clearICs(mLockKey.getLockId(), listener);
    }

    @Override
    public void deleteIC(long cardNumber) {
        if (MyLockAPI.getLockAPI().isConnected(mLockKey.getLockMac())) {
            MyLockAPI.getLockAPI().deleteICCard(null,cardNumber, mLockKey);
        } else {
            Bundle bundle = new Bundle();
            bundle.putLong(Constant.IC_CARD_NUMBER,cardNumber);
            MyLockAPI.sBleSession.setArgments(bundle);
            MyLockAPI.getLockAPI().connect(mLockKey.getLockMac(), Operation.DELETE_IC_CARD);
        }
    }

    @Override
    public void deleteICFromServer(int cardId,OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().deleteIC(mLockKey.getLockId(),cardId,listener);
    }
}
