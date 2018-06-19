package cn.jcyh.eaglelock.function.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.LockClockContract;
import cn.jcyh.eaglelock.function.model.LockClockModel;
import cn.jcyh.eaglelock.util.T;
import cn.jcyh.eaglelock.util.Util;
import cn.jcyh.locklib.entity.Error;

public class LockClockPresenter extends BasePresenter<LockClockContract.View, LockClockContract.Model> implements LockClockContract.Presenter {
    private MyReceiver mReceiver;

    public LockClockPresenter(LockKey lockKey) {
        mModel.setLockKey(lockKey);
    }

    @Override
    public void registerReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter(Constant.ACTION_LOCK_GET_TIME);
        intentFilter.addAction(Constant.ACTION_LOCK_SYNC_TIME);
        LocalBroadcastManager.getInstance(Util.getApp()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void getLockTime() {
        mView.showProgressDialog();
        mModel.getLockTime();
    }

    @Override
    public void setLockTime() {
        mView.showProgressDialog();
        mModel.setLockTime();
    }

    @Override
    public LockClockContract.Model attacheModel() {
        return new LockClockModel();
    }


    @Override
    public void detachView() {
        super.detachView();
        LocalBroadcastManager.getInstance(Util.getApp()).unregisterReceiver(mReceiver);
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action) || mView == null) return;
            Error error = (Error) intent.getSerializableExtra(Constant.ERROR_MSG);
            switch (action) {
                case Constant.ACTION_LOCK_GET_TIME:
                    if (Error.SUCCESS == error) {
                        long date = intent.getLongExtra(Constant.DATE, 0);
                        String lockDate = SimpleDateFormat.getInstance().format(new Date(date));
                        mView.getLockTimeSuccess(lockDate);
                    } else {
                        T.show(error.getDescription());
                    }
                    mView.cancelProgressDialog();
                    break;
                case Constant.ACTION_LOCK_SYNC_TIME:
                    if (Error.SUCCESS == error) {
                        T.show(R.string.calibration_success);
                    } else {
                        T.show(error.getDescription());
                    }
                    mView.cancelProgressDialog();
                    break;
            }
        }
    }
}
