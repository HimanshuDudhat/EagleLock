package cn.jcyh.eaglelock.function.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.jcyh.eaglelock.R;
import cn.jcyh.locklib.scanner.ExtendedBluetoothDevice;

/**
 * Created by jogger on 2018/6/12.可添加锁列表
 */
public class AddLockItemAdapter extends BaseQuickAdapter<ExtendedBluetoothDevice, BaseViewHolder> {

    public AddLockItemAdapter(@Nullable List<ExtendedBluetoothDevice> data) {
        super(R.layout.rv_addlock_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExtendedBluetoothDevice item) {
        helper.setText(R.id.tv_device_name, item.getName());
        helper.addOnClickListener(R.id.tv_add);
        helper.setVisible(R.id.tv_add, item.isSettingMode());
    }

    public void updateDevice(ExtendedBluetoothDevice device) {
        boolean contain = false;
        boolean update = false;
        for (ExtendedBluetoothDevice d : mData) {
            if (d.equals(device)) {
                contain = true;
                if (d.isSettingMode() != device.isSettingMode()) {
                    d.setSettingMode(device.isSettingMode());
                    update = true;
                }
            }
        }
        if (!contain) {
            mData.add(device);
            update = true;
        }
        if (update)
            notifyDataSetChanged();
    }
}
