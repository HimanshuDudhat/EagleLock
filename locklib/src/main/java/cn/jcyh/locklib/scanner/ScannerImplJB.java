//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.jcyh.locklib.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.RequiresPermission;

import java.util.UUID;

import cn.jcyh.locklib.service.BluetoothLeService;
import cn.jcyh.locklib.util.LogUtil;

public class ScannerImplJB extends ScannerCompat implements LeScanCallback {
    private static boolean DBG = true;
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public ScannerImplJB() {
    }

    @RequiresPermission(
            allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"}
    )
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        this.mIScanCallback.onScan(new ExtendedBluetoothDevice(device, rssi, scanRecord));
    }

    @RequiresPermission(
            allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"}
    )
    public void startScanInternal(UUID[] serviceUuids) {
        LogUtil.d(this.toString(), DBG);
        if(BluetoothLeService.scanBongOnly) {
            this.mBluetoothAdapter.startLeScan(this);
        } else {
            this.mBluetoothAdapter.startLeScan(serviceUuids, this);
        }

    }

    @RequiresPermission(
            allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"}
    )
    public void stopScan() {
        try {
            if(this.mBluetoothAdapter.isEnabled()) {
                LogUtil.d(this.toString(), DBG);
                this.mBluetoothAdapter.stopLeScan(this);
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}
