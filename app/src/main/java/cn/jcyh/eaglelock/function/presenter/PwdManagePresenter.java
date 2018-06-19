package cn.jcyh.eaglelock.function.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.entity.LockPwdRecord;
import cn.jcyh.eaglelock.function.contract.PwdManageContract;
import cn.jcyh.eaglelock.function.model.PwdManageModel;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.T;
import cn.jcyh.eaglelock.util.Util;
import cn.jcyh.locklib.entity.Error;

/**
 * Created by jogger on 2018/6/19.
 */
public class PwdManagePresenter extends BasePresenter<PwdManageContract.View, PwdManageContract.Model> implements PwdManageContract.Presenter {
    private MyReceiver mReceiver;

    public PwdManagePresenter(LockKey lockKey) {
        mModel.setLockKey(lockKey);
    }

    @Override
    public void registerReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter(Constant.ACTION_RESET_PWD);
        LocalBroadcastManager.getInstance(Util.getApp()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void getPwdRecords(int pageNo, int pageSize) {
        mModel.getPwdRecords(pageNo, pageSize, new OnHttpRequestListener<LockHttpResult<LockPwdRecord>>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
            }

            @Override
            public void onSuccess(LockHttpResult<LockPwdRecord> lockPwdRecordLockHttpResult) {
                if (mView == null) return;
                mView.getPwdRecordsSuccess(lockPwdRecordLockHttpResult);
            }
        });
    }

    @Override
    public void resetPwd() {
        mView.showProgressDialog();
        mModel.resetPwd();
    }

    @Override
    public PwdManageContract.Model attacheModel() {
        return new PwdManageModel();
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
            switch (action) {
                case Constant.ACTION_RESET_PWD:
                    Error error = (Error) intent.getSerializableExtra(Constant.ERROR_MSG);
                    if (Error.SUCCESS == error) {
                        resetFromServer(intent.getStringExtra("pwdInfo"), intent.getLongExtra("timestamp", 0));
                    } else {
                        T.show(error.getDescription());
                        mView.cancelProgressDialog();
                    }
                    break;
            }
        }
    }

    private void resetFromServer(String pwdInfo, long timestamp) {
        mModel.resetFromServer(pwdInfo, timestamp, new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
                T.show(R.string.reset_failure, errorCode);
                mView.cancelProgressDialog();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                mView.cancelProgressDialog();
                mView.resetPwdSuccess();
            }
        });
    }
}
