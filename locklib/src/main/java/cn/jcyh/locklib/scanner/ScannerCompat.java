//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.jcyh.locklib.scanner;

import android.os.Build.VERSION;

import java.util.UUID;

import cn.jcyh.locklib.service.ThreadPool;

public abstract class ScannerCompat {
    private static boolean DBG = true;
    private static ScannerCompat mInstance;
    protected static String UUID_SERVICE = "00001910-0000-1000-8000-00805f9b34fb";
    protected static UUID[] serviceUuids;
    protected IScanCallback mIScanCallback;
    protected boolean scanBongOnly;

    public ScannerCompat() {
    }

    public static ScannerCompat getScanner() {
        return mInstance != null?mInstance:(VERSION.SDK_INT >= 21?(mInstance = new ScannerLollipop()):(mInstance = new ScannerImplJB()));
    }

    public void startScan(final IScanCallback scanCallback) {
        ThreadPool.getThreadPool().execute(new Runnable() {
            public void run() {
                ScannerCompat.this.mIScanCallback = scanCallback;
                ScannerCompat.this.startScanInternal(ScannerCompat.serviceUuids);
            }
        });
    }

    public abstract void startScanInternal(UUID[] var1);

    public abstract void stopScan();

    public void setScanBongOnly(boolean scanBongOnly) {
        this.scanBongOnly = scanBongOnly;
    }

    static {
        serviceUuids = new UUID[]{UUID.fromString(UUID_SERVICE)};
    }
}
