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
import cn.jcyh.eaglelock.function.contract.SettingContract;
import cn.jcyh.eaglelock.function.model.SettingModel;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.T;
import cn.jcyh.eaglelock.util.Util;
import cn.jcyh.locklib.entity.Error;

public class SettingPresenter extends BasePresenter<SettingContract.View, SettingContract.Model> implements SettingContract.Presenter {
    private MyReceiver mReceiver;

    public SettingPresenter(LockKey lockKey) {
        mModel.setLockKey(lockKey);
    }

    @Override
    public void registerReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter(Constant.ACTION_RESET_LOCK);
        intentFilter.addAction(Constant.ACTION_SET_ADMIN_PWD);
        LocalBroadcastManager.getInstance(Util.getApp()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public SettingContract.Model attacheModel() {
        return new SettingModel();
    }

    @Override
    public void detachView() {
        super.detachView();
        LocalBroadcastManager.getInstance(Util.getApp()).unregisterReceiver(mReceiver);
    }

    @Override
    public void rename(final String content) {
        mModel.rename(content, new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
                T.show(Util.getApp().getString(R.string.edit_failure) + errorCode);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                T.show(Util.getApp().getString(R.string.edit_success));
                mView.onRenameSuccess(content);
            }
        });
    }

    @Override
    public void setAdminKeyboardPassword(String content) {
        mView.showProgressDialog();
        mModel.setAdminKeyboardPassword(content);
    }

    /**
     * 删除钥匙
     */
    @Override
    public void deleteLockKey() {
        mView.showProgressDialog();
        mModel.deleteLockKey();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action) || mView == null) return;
            Error error = (Error) intent.getSerializableExtra(Constant.ERROR_MSG);
            switch (action) {
                case Constant.ACTION_RESET_LOCK:
                    if (Error.SUCCESS == error) {
                        deleteFromServer();
                    } else {
                        mView.cancelProgressDialog();
                        T.show(error.getDescription());
                    }
                    break;
                case Constant.ACTION_SET_ADMIN_PWD:
                    if (Error.SUCCESS == error) {
                        setAdminPwd(intent.getStringExtra("password"));
                    } else {
                        T.show(error.getDescription());
                        mView.cancelProgressDialog();
                    }
                    break;
            }
        }
    }

    /**
     * 从服务器中删除
     */
    private void deleteFromServer() {
        mModel.deleteLockKeyFromServer(new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                T.show(R.string.delete_failure, errorCode);
                if (mView == null) return;
                mView.cancelProgressDialog();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                T.show(R.string.edit_success);
                if (mView == null) return;
                mView.cancelProgressDialog();
                mView.onDeleteLockKeySuccess();
            }
        });
    }

    /**
     * 设置管理员键盘密码
     */
    private void setAdminPwd(final String password) {
        mModel.changeAdminKeyboardPwd(password, new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                T.show(R.string.edit_failure, errorCode);
                if (mView == null) return;
                mView.cancelProgressDialog();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                T.show(R.string.edit_success);
                if (mView == null) return;
                mView.cancelProgressDialog();
                mView.onSetAdminPwdSuccess(password);
            }
        });
    }
}
