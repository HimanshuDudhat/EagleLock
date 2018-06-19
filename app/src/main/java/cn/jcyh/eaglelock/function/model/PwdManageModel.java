package cn.jcyh.eaglelock.function.model;

import cn.jcyh.eaglelock.constant.Operation;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.entity.LockPwdRecord;
import cn.jcyh.eaglelock.function.contract.PwdManageContract;
import cn.jcyh.eaglelock.http.LockHttpAction;
import cn.jcyh.eaglelock.http.api.MyLockAPI;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;

/**
 * Created by jogger on 2018/6/19.
 */
public class PwdManageModel implements PwdManageContract.Model {
    private LockKey mLockKey;

    @Override
    public void setLockKey(LockKey lockKey) {
        mLockKey = lockKey;
    }

    @Override
    public void getPwdRecords(int pageNo, int pageSize, OnHttpRequestListener<LockHttpResult<LockPwdRecord>> listener) {
        LockHttpAction.getHttpAction().getPwdsByLock(mLockKey.getLockId(), pageNo, pageSize, listener);
    }

    @Override
    public void resetPwd() {
        MyLockAPI lockAPI = MyLockAPI.getLockAPI();
        if (lockAPI.isConnected(mLockKey.getLockMac()))
            lockAPI.resetKeyboardPassword(null, mLockKey);
        else
            lockAPI.connect(mLockKey.getLockMac(), Operation.RESET_KEYBOARD_PASSWORD);
    }

    @Override
    public void resetFromServer(String pwdInfo, long timestamp, OnHttpRequestListener<Boolean> listener) {
        LockHttpAction.getHttpAction().resetPwd(mLockKey.getLockId(), pwdInfo, timestamp, listener);
    }
}
