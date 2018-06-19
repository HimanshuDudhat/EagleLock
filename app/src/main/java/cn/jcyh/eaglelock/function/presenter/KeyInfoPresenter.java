package cn.jcyh.eaglelock.function.presenter;

import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.KeyInfoContract;
import cn.jcyh.eaglelock.function.model.KeyInfoModel;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.T;

/**
 * Created by jogger on 2018/6/19.
 */
public class KeyInfoPresenter extends BasePresenter<KeyInfoContract.View, KeyInfoContract.Model> implements KeyInfoContract.Presenter {
    public KeyInfoPresenter(LockKey lockKey) {
        mModel.setLockKey(lockKey);
    }

    @Override
    public void rename(String name) {

    }

    @Override
    public void authKeyUser() {
        mModel.authKeyUser(new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                T.show(R.string.authorize_failure);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                T.show(R.string.authorize_success);
                mView.authKeyUserSuccess();
            }
        });
    }

    @Override
    public void unAuthKeyUser() {
        mModel.unAuthKeyUser(new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                T.show(R.string.unauth_failure);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                T.show(R.string.unauth_success);
                mView.unAuthKeyUserSuccess();
            }
        });
    }

    @Override
    public void freezeKey() {
        mModel.freezeKey(new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                T.show(R.string.freezekey_failure);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                T.show(R.string.freezekey_success);
                mView.freezeKeySuccess();
            }
        });
    }

    @Override
    public void unFreezeKey() {
        mModel.unFreezeKey(new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
                T.show(R.string.unfreezekey_failure);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                T.show(R.string.unfreezekey_success);
                mView.unFreezeKeySuccess();
            }
        });
    }

    @Override
    public void deleteKey() {
        mView.showProgressDialog();
        mModel.deleteKey(new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                if (mView == null) return;
                mView.cancelProgressDialog();
                T.show(R.string.delete_failure);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                mView.cancelProgressDialog();
                T.show(R.string.delete_success);
                mView.deleteKeySuccess();

            }
        });
    }

    @Override
    public KeyInfoContract.Model attacheModel() {
        return new KeyInfoModel();
    }
}
