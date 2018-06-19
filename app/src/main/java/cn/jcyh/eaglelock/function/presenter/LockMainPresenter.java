package cn.jcyh.eaglelock.function.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.LockMainContract;
import cn.jcyh.eaglelock.function.model.LockMainModel;
import cn.jcyh.eaglelock.util.Util;
import cn.jcyh.locklib.entity.Error;

/**
 * Created by jogger on 2018/6/13.
 */
public class LockMainPresenter extends BasePresenter<LockMainContract.View, LockMainContract.Model> implements LockMainContract.Presenter {
    private MyReceiver mReceiver;

    public LockMainPresenter(LockKey lockKey) {
        mModel.setLockKey(lockKey);
    }

    @Override
    public void registerReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_UNLOCK);
        LocalBroadcastManager.getInstance(Util.getApp()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void unlock() {
        mModel.unlock();
    }


    @Override
    public void detachView() {
        super.detachView();
        LocalBroadcastManager.getInstance(Util.getApp()).unregisterReceiver(mReceiver);
    }

    @Override
    public LockMainModel attacheModel() {
        return new LockMainModel();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) return;
            switch (action) {
                case Constant.ACTION_UNLOCK:
                    Error error = (Error) intent.getSerializableExtra(Constant.ERROR_MSG);
                    if (mView != null)
                        mView.unLockResult(error);
                    break;
            }
        }
    }
}
