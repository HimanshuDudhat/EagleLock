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
import cn.jcyh.eaglelock.function.contract.KeyManageContract;
import cn.jcyh.eaglelock.function.model.KeyManageModel;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.L;
import cn.jcyh.eaglelock.util.T;
import cn.jcyh.eaglelock.util.Util;
import cn.jcyh.locklib.entity.Error;

public class KeyManagePresenter extends BasePresenter<KeyManageContract.View, KeyManageContract.Model> implements KeyManageContract.Presenter {
    private MyReceiver mReceiver;

    public KeyManagePresenter(LockKey lockKey) {
        mModel.setLockKey(lockKey);
    }

    @Override
    public void registerReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter(Constant.ACTION_RESET_KEY);
        LocalBroadcastManager.getInstance(Util.getApp()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void getLockKeys(int pageNo, int pageSize) {
        mModel.getLockKeys(pageNo, pageSize, new OnHttpRequestListener<LockHttpResult<LockKey>>() {
            @Override
            public void onFailure(int errorCode) {
                L.e("----------errorCode:" + errorCode);
            }

            @Override
            public void onSuccess(LockHttpResult<LockKey> lockKeyLockHttpResult) {
                L.e("----------onSuccess:" + lockKeyLockHttpResult);
                if (mView == null || lockKeyLockHttpResult == null) return;
                mView.getLockKeysSuccess(lockKeyLockHttpResult);
            }
        });
    }

    @Override
    public void resetKey() {
        mView.showProgressDialog();
        mModel.resetKey();
    }

    @Override
    public void clearKey() {
        mView.showProgressDialog();
        mModel.clearKey(new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
                mView.cancelProgressDialog();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                mView.cancelProgressDialog();
                mView.clearKeySuccess();
            }
        });
    }

    @Override
    public KeyManageContract.Model attacheModel() {
        return new KeyManageModel();
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
            if (TextUtils.isEmpty(action)) return;
            switch (action) {
                case Constant.ACTION_RESET_KEY:
                    Error error = (Error) intent.getSerializableExtra(Constant.ERROR_MSG);
                    if (Error.SUCCESS == error) {
                        resetFromServer();
                    } else {
                        T.show(error.getDescription());
                        mView.cancelProgressDialog();
                    }
                    break;
            }
        }
    }

    private void resetFromServer() {
        mModel.resetKeyFromServer(new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
                mView.cancelProgressDialog();
                T.show(R.string.reset_failure, errorCode);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                mView.cancelProgressDialog();
                T.show(R.string.reset_success);
                mView.resetKeySuccess();
            }
        });
    }
}
