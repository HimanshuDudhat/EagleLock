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

/**
 * Created by jogger on 2018/6/12.
 */
public class MainPresenter extends BasePresenter<MainContract.View, MainContract.Model> implements MainContract.Presenter {
    public MainPresenter() {
        attachModel(new MainModel());
    }

    @Override
    public void syncDatas() {
        final User user = ControlCenter.getControlCenter().getUserInfo();
        if (user == null) return;
//        ControlCenter.getControlCenter().getLastSyncDate()
        mModel.syncDatas(0, new OnHttpRequestListener<SyncData>() {
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
}
