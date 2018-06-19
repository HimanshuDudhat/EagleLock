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
import cn.jcyh.eaglelock.function.contract.ICManageContract;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.T;
import cn.jcyh.eaglelock.util.Util;
import cn.jcyh.locklib.entity.Error;
import cn.jcyh.locklib.entity.ICCard;

/**
 * Created by jogger on 2018/6/19.
 */
public class ICManagePresenter extends BasePresenter<ICManageContract.View, ICManageContract.Model> implements ICManageContract.Presenter {
    private MyReceiver mReceiver;

    public ICManagePresenter(LockKey lockKey) {
        mModel.setLockKey(lockKey);
    }

    @Override
    public void registerReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter(Constant.ACTION_LOCK_IC_CARD);
        LocalBroadcastManager.getInstance(Util.getApp()).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void getICDatas(int pageNo, int pageSize) {
        mModel.getICDatas(pageNo, pageSize, new OnHttpRequestListener<LockHttpResult<ICCard>>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
            }

            @Override
            public void onSuccess(LockHttpResult<ICCard> icCardLockHttpResult) {
                if (mView == null) return;
                mView.getICDatasSuccess(icCardLockHttpResult);
            }
        });
    }

    @Override
    public void clearIC() {
        mView.showProgressDialog();
        mModel.clearIC();
    }

    @Override
    public void deleteIC(long cardNumber) {
        mView.showProgressDialog();
        mModel.deleteIC(cardNumber);
    }

    @Override
    public ICManageContract.Model attacheModel() {
        return null;
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
            if (!Constant.ACTION_LOCK_IC_CARD.equals(action) || mView == null) return;
            String type = intent.getStringExtra(Constant.TYPE);
            Error error = (Error) intent.getSerializableExtra(Constant.ERROR_MSG);
            switch (type) {
                case Constant.TYPE_MODIFY_IC_CARD:
                    break;
                case Constant.TYPE_DELETE_IC_CARD:
                    if (Error.SUCCESS == error) {
                        deleteFromServer();
                    } else {
                        T.show(error.getDescription());
                        mView.cancelProgressDialog();
                    }
                    break;
                case Constant.TYPE_CLEAR_IC_CARD:
                    if (Error.SUCCESS == error) {
                        clearFromServer();
                    } else {
                        T.show(error.getDescription());
                        mView.cancelProgressDialog();
                    }
                    break;
            }

        }
    }

    private void clearFromServer() {
        mModel.clearICFromServer(new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
                mView.cancelProgressDialog();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                mView.cancelProgressDialog();
            }
        });
    }

    private void deleteFromServer() {
        mModel.deleteICFromServer(mView.getCardID(), new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
                mView.cancelProgressDialog();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                mView.cancelProgressDialog();

            }
        });
    }
}
