package cn.jcyh.eaglelock.function.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.base.recyclerview.DividerLinearItemDecoration;
import cn.jcyh.eaglelock.base.recyclerview.MyLinearLayoutManager;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.constant.Operation;
import cn.jcyh.eaglelock.function.adapter.AddLockItemAdapter;
import cn.jcyh.eaglelock.http.api.MyLockAPI;
import cn.jcyh.eaglelock.util.T;
import cn.jcyh.locklib.entity.Error;
import cn.jcyh.locklib.scanner.ExtendedBluetoothDevice;

public class AddLockActivity extends BaseActivity implements AddLockItemAdapter.OnItemChildClickListener {
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    private AddLockItemAdapter mAdapter;
    private List<ExtendedBluetoothDevice> mBluetoothDevices;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {
        rvContent.setLayoutManager(new MyLinearLayoutManager(this));
        rvContent.addItemDecoration(new DividerLinearItemDecoration(this));
        mAdapter = new AddLockItemAdapter(mBluetoothDevices);
        mAdapter.setOnItemChildClickListener(this);
        rvContent.setAdapter(mAdapter);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_BLE_DEVICE);
        intentFilter.addAction(Constant.ACTION_BLE_DISCONNECTED);
        intentFilter.addAction(Constant.ACTION_ADD_ADMIN);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_lock;
    }

    @OnClick(R.id.ibtn_return)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ExtendedBluetoothDevice bluetoothDevice = (ExtendedBluetoothDevice) adapter.getItem(position);
        if (bluetoothDevice == null) return;
        MyLockAPI.getLockAPI().connect(bluetoothDevice, Operation.ADD_ADMIN);
        showProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Constant.ACTION_BLE_DEVICE.equals(action)) {
                Bundle bundle = intent.getExtras();
                assert bundle != null;
                ExtendedBluetoothDevice device = bundle.getParcelable(Constant.DEVICE);
                mAdapter.updateDevice(device);
            } else if (Constant.ACTION_ADD_ADMIN.equals(action)) {
                cancelProgressDialog();
                Error error = (Error) intent.getSerializableExtra(Constant.ERROR_MSG);
                if (Error.SUCCESS.equals(error)) {
                    startNewActivity(MainActivity.class);
                    T.show(error.getDescription());
                    finish();
                } else {
                    T.show(error.getDescription());
                }
            }
//            else if(action.equals(BleConstant.ACTION_BLE_DISCONNECTED)) {
//                cancelProgressDialog();
//                Toast.makeText(FoundDeviceActivity.this, "蓝牙已断开,请重新添加.", Toast.LENGTH_LONG).show();
//            }
        }
    };
}
