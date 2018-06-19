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
import cn.jcyh.eaglelock.entity.LockKeyboardPwd;
import cn.jcyh.eaglelock.function.contract.SendPwdItemContract;
import cn.jcyh.eaglelock.function.model.SendPwdItemModel;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.L;
import cn.jcyh.eaglelock.util.T;
import cn.jcyh.eaglelock.util.Util;
import cn.jcyh.locklib.entity.Error;

public class SendPwdItemPresenter extends BasePresenter<SendPwdItemContract.View, SendPwdItemContract.Model> implements SendPwdItemContract.Presenter {
    private MyReceiver mReceiver;

    public SendPwdItemPresenter(LockKey lockKey) {
        super();
        mModel.setLockKey(lockKey);
    }

    @Override
    public void registerReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CUSTOM_PWD);
        LocalBroadcastManager.getInstance(Util.getApp()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void addPeriodKeyboardPassword(String keyboardPwd, long startTime, long endTime) {
        mModel.addPeriodKeyboardPassword(keyboardPwd, startTime, endTime);
    }

    @Override
    public void getPwd(int keyboardPwdType, long startTime, long endTime) {
        mModel.getPwd(keyboardPwdType, startTime, endTime, new OnHttpRequestListener<LockKeyboardPwd>() {
            @Override
            public void onFailure(int errorCode) {
                L.e("----------onFailure" + errorCode);
            }

            @Override
            public void onSuccess(LockKeyboardPwd lockKeyboardPwd) {
                L.e("----------lockeyboardPwd:" + lockKeyboardPwd);
                if (mView == null || lockKeyboardPwd == null) return;
                mView.getPwdSuccess(lockKeyboardPwd);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        LocalBroadcastManager.getInstance(Util.getApp()).unregisterReceiver(mReceiver);
    }

    @Override
    public SendPwdItemContract.Model attacheModel() {
        return new SendPwdItemModel();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mView == null) return;
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) return;
            switch (action) {
                case Constant.ACTION_CUSTOM_PWD:
                    Error error = (Error) intent.getSerializableExtra(Constant.ERROR_MSG);
                    if (!error.getErrorCode().equals(Error.SUCCESS.getErrorCode())) {
                        T.show(error.getDescription());
                        mView.cancelProgressDialog();
                        return;
                    }
                    mModel.customPwd(mView.getKeyboardPwd(), mView.getStartTime(), mView.getEndTime(), new OnHttpRequestListener<LockKeyboardPwd>() {
                        @Override
                        public void onFailure(int errorCode) {
                            if (mView == null) return;
                            mView.cancelProgressDialog();
                            T.show(R.string.setting_failure, errorCode);
                        }

                        @Override
                        public void onSuccess(LockKeyboardPwd lockKeyboardPwd) {
                            if (mView == null) return;
                            mView.cancelProgressDialog();
                            T.show(R.string.setting_success);
                            mView.customPwdSuccess();
                        }
                    });
                    break;
            }
        }
    }
}
