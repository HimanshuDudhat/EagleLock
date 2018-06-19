package cn.jcyh.eaglelock.function.presenter;

import java.util.List;

import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.control.ControlCenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.entity.SyncData;
import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.function.contract.MainContract;
import cn.jcyh.eaglelock.function.model.MainModel;
import cn.jcyh.eaglelock.http.listener.OnHttpRequestListener;
import cn.jcyh.eaglelock.util.L;

public class MainPresenter extends BasePresenter<MainContract.View, MainContract.Model> implements MainContract.Presenter {

    @Override
    public void syncDatas() {
        final User user = ControlCenter.getControlCenter().getUserInfo();
        if (user == null) return;
//        L.e("---------token:" + user.getAccess_token());
        final List<LockKey> lockKeys = ControlCenter.getControlCenter().getLockKeys();
        long lastUpdateDate=0;
//        if (lockKeys != null && lockKeys.size() != 0) {
//            //判断是不是上次登录的用户
////            L.e("--------lockKey getAccessToken" + lockKeys.get(0).getAccessToken());
////            L.e("----------" + (lockKeys.get(0).getAccessToken().equals(user.getAccess_token())));
//            if (!lockKeys.get(0).getAccessToken().equals(user.getAccess_token())) {
//                lastUpdateDate = 0;
////                ControlCenter.getControlCenter().saveLockKeys(null);
//            } else {
//                mView.syncDataSuccess(lockKeys);
//                return;
//            }
//        } else {
//            lastUpdateDate = 0;
//        }
        sync(user, lastUpdateDate);
    }

    private void sync(final User user, long lastUpdateDate) {
        mModel.syncDatas(lastUpdateDate, new OnHttpRequestListener<SyncData>() {
            @Override
            public void onFailure(int errorCode) {
                L.e("------------errorCode:" + errorCode);
            }

            @Override
            public void onSuccess(SyncData syncData) {
                L.e("------------keylist:" + syncData);
                if (syncData == null) return;
                List<LockKey> keyList = syncData.getKeyList();
                if (keyList == null || keyList.size() == 0) return;
                for (int i = 0; i < keyList.size(); i++) {
                    LockKey key = syncData.getKeyList().get(i);
                    key.setAccessToken(user.getAccess_token());
                }
                ControlCenter controlCenter = ControlCenter.getControlCenter();
                controlCenter.saveLastSyncDate(syncData.getLastUpdateDate());
                controlCenter.saveLockKeys(keyList);
                mView.syncDataSuccess(keyList);
            }
        });
    }

    @Override
    public MainContract.Model attacheModel() {
        return new MainModel();
    }
}
