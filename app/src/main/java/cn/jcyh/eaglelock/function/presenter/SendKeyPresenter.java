package cn.jcyh.eaglelock.function.presenter;

import android.text.TextUtils;

import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.constant.Config;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.SendKeyContract;
import cn.jcyh.eaglelock.function.model.SendKeyModel;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.T;

public class SendKeyPresenter extends BasePresenter<SendKeyContract.View, SendKeyContract.Model> implements SendKeyContract.Presenter {
    private static final int LIMIT_TIME = 0;
    private static final int FORVER = 1;
    private static final int ONCE = 2;

    public SendKeyPresenter(LockKey lockKey) {
        mModel.setLockKey(lockKey);
    }

    @Override
    public void sendKey() {
        String account = mView.getReceiveAccount();
        long startTime = mView.getStartTime();
        long endTime = mView.getEndTime();
        if (TextUtils.isEmpty(account)) {
            T.show(R.string.input_can_not_null);
            return;
        }
        if (mView.getCurrentType() == LIMIT_TIME) {
            if (mView.getEndTime() - mView.getStartTime() <= 0) {
                T.show(R.string.time_choose_error);
                return;
            }
        }
        mView.showProgressDialog();
        if (mView.getCurrentType() == FORVER) {
            startTime = 0;
            endTime = 0;
        } else if (mView.getCurrentType() == ONCE) {
            startTime = System.currentTimeMillis();
            endTime = 1;
        }
        mModel.sendKey(Config.EAGLEKING + account, startTime, endTime, "", new OnHttpRequestListener<Boolean>() {
            @Override
            public void onFailure(int errorCode) {
                T.show(R.string.send_failure, errorCode);
                mView.cancelProgressDialog();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (mView == null) return;
                T.show(R.string.send_success);
                mView.cancelProgressDialog();
                mView.sendKeySuccess();
            }
        });
    }

    @Override
    public SendKeyContract.Model attacheModel() {
        return new SendKeyModel();
    }
}
