package cn.jcyh.eaglelock.function.ui;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.base.recyclerview.MainGridItemDecoration;
import cn.jcyh.eaglelock.base.recyclerview.MyGridLayoutManager;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.adapter.MainAdapter;
import cn.jcyh.eaglelock.function.contract.MainContract;
import cn.jcyh.eaglelock.function.presenter.MainPresenter;
import cn.jcyh.eaglelock.http.api.MyLockAPI;
import cn.jcyh.eaglelock.util.L;
import cn.jcyh.eaglelock.util.SizeUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.tv_devices)
    TextView tvDevices;
    @BindView(R.id.et_search)
    EditText etSearch;
    private MainAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void init() {
        rvContent.setLayoutManager(new MyGridLayoutManager(this, 2));
        rvContent.addItemDecoration(new MainGridItemDecoration(2, SizeUtil.dp2px(16)));
        mAdapter = new MainAdapter(null);
        mAdapter.setOnItemClickListener(this);
        rvContent.setAdapter(mAdapter);
        MyLockAPI.getLockAPI().startBleService(this);
        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable subscribe = rxPermissions
                .requestEach(Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            L.e("用户同意该权限" + permission.name);
                            MyLockAPI.getLockAPI().startBTDeviceScan();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            L.e("用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框");
                        } else {
                            L.e("----------用户拒绝了该权限，并且选中『不再询问』");
                        }
                    }
                });
        mPresenter.syncDatas();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPresenter.syncDatas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.syncDatas();
    }

    @Override
    public void syncDataSuccess(List<LockKey> keyList) {
        tvDevices.setText(String.format(getString(R.string.device_num_format), keyList.size()));
        mAdapter.setNewData(keyList);
    }

    @OnClick({R.id.ibtn_user, R.id.ibtn_search, R.id.ibtn_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_user:
                break;
            case R.id.ibtn_search:
                etSearch.setVisibility(etSearch.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                break;
            case R.id.ibtn_add:
                startNewActivity(AddLockHintActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLockAPI.getLockAPI().stopBleService(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        LockKey lockKey = (LockKey) adapter.getItem(position);
        if (lockKey == null) return;
        startNewActivity(LockMainActivity.class, Constant.LOCK_KEY, lockKey);
    }
}
