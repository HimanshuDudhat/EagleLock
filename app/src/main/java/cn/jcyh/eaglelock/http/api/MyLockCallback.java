package cn.jcyh.eaglelock.http.api;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import java.util.List;
import java.util.TimeZone;

import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.constant.Operation;
import cn.jcyh.eaglelock.control.ControlCenter;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.entity.User;
import cn.jcyh.eaglelock.util.L;
import cn.jcyh.eaglelock.util.T;
import cn.jcyh.eaglelock.util.Util;
import cn.jcyh.locklib.callback.LockCallback;
import cn.jcyh.locklib.entity.Error;
import cn.jcyh.locklib.scanner.ExtendedBluetoothDevice;


/**
 * Created by jogger on 2018/4/27.
 * 锁回调
 */

public class MyLockCallback implements LockCallback {
    private User mUser;
    private final ControlCenter mControlCenter;
    private LocalBroadcastManager mLocalBroadcastManager;

    public MyLockCallback() {
        mControlCenter = ControlCenter.getControlCenter();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(Util.getApp());
    }

    @Override
    public void onFoundDevice(ExtendedBluetoothDevice extendedBluetoothDevice) {
        L.i("------------onFoundDevice");
        mUser = mControlCenter.getUserInfo();
        if (mUser == null || TextUtils.isEmpty(mUser.getAccess_token())) {
//            mContext.startActivity(new Intent(mContext, AuthActivity.class));
            return;
        }
        //发现设备并广播
        Intent intent = new Intent(Constant.ACTION_BLE_DEVICE);
        intent.putExtra(Constant.DEVICE, extendedBluetoothDevice);
        LocalBroadcastManager.getInstance(Util.getApp()).sendBroadcast(intent);
//            String accessToken = MyPreference.getStr(mContext, MyPreference.ACCESS_TOKEN);
        //根据accessToken和lockmac获取钥匙
        List<LockKey> keys = mControlCenter.getLockKeys();
        if (keys == null || keys.size() == 0) return;
        LockKey localKey = null;
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i) == null) return;
            if (TextUtils.isEmpty(keys.get(i).getAccessToken()) || TextUtils.isEmpty(keys.get(i).getLockMac()))
                continue;
            if (keys.get(i).getAccessToken().equals(mUser.getAccess_token()) && keys.get(i).getLockMac().equals(extendedBluetoothDevice.getAddress())) {
                localKey = keys.get(i);
                break;
            }
        }
        if (localKey != null) {
            switch (MyLockAPI.sBleSession.getOperation()) {
                case Operation.LOCKCAR_DOWN:
                    if (extendedBluetoothDevice.isTouch())
                        MyLockAPI.getLockAPI().connect(extendedBluetoothDevice);
                    break;
                case Operation.CUSTOM_PWD:
                case Operation.SET_ADMIN_KEYBOARD_PASSWORD:
                case Operation.SET_DELETE_PASSWORD:
                case Operation.SET_LOCK_TIME:
                case Operation.RESET_KEYBOARD_PASSWORD:
                case Operation.RESET_EKEY:
                case Operation.RESET_LOCK:
                case Operation.GET_LOCK_TIME:
                case Operation.GET_OPERATE_LOG:
                    if (extendedBluetoothDevice.getAddress().equals(MyLockAPI.sBleSession.getLockmac()))
                        MyLockAPI.getLockAPI().connect(extendedBluetoothDevice);
                    break;
                default:
                    if (extendedBluetoothDevice.getAddress().equals(MyLockAPI.sBleSession.getLockmac()))
                        MyLockAPI.getLockAPI().connect(extendedBluetoothDevice);
                    break;
            }
        }
    }

    @Override
    public void onDeviceConnected(ExtendedBluetoothDevice extendedBluetoothDevice) {
        L.e("--------onDeviceConnected:" + MyLockAPI.sBleSession.getOperation());
        MyLockAPI lockAPI = MyLockAPI.getLockAPI();
        mUser = mControlCenter.getUserInfo();
        L.e("-------mUser:" + mUser);
        if (mUser == null || TextUtils.isEmpty(mUser.getAccess_token())) {
            return;
        }
        //根据accessToken和lockmac获取钥匙
        List<LockKey> keys = mControlCenter.getLockKeys();
        if (keys == null && Operation.ADD_ADMIN.equals(MyLockAPI.sBleSession.getOperation()))
            return;
        LockKey localKey = null;
        assert keys != null;
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i) == null) return;
            if (TextUtils.isEmpty(keys.get(i).getAccessToken()) || TextUtils.isEmpty(keys.get(i).getLockMac()))
                continue;
            if (keys.get(i).getAccessToken().equals(mUser.getAccess_token()) && keys.get(i).getLockMac().equals(extendedBluetoothDevice.getAddress())) {
                localKey = keys.get(i);
                break;
            }
        }
        switch (MyLockAPI.sBleSession.getOperation()) {
            case Operation.ADD_ADMIN:
                L.e("----------ADD_ADMIN");
                lockAPI.addAdministrator(extendedBluetoothDevice);
                break;
            case Operation.LOCKCAR_DOWN:
                L.e("--------------UNLOCK" + localKey);
                if (localKey == null) return;
                //本地存在锁
                if (localKey.isAdmin()) {
                    lockAPI.unlockByAdministrator(extendedBluetoothDevice, localKey);
                } else {
                    lockAPI.unlockByUser(extendedBluetoothDevice, localKey);
                }
                break;
            case Operation.SET_ADMIN_KEYBOARD_PASSWORD://管理码
                lockAPI.setAdminKeyboardPassword(extendedBluetoothDevice, localKey, MyLockAPI.sBleSession.getArgments().getString("password"));
                break;
            case Operation.CUSTOM_PWD:
                if (localKey == null) return;
                Bundle argments = MyLockAPI.sBleSession.getArgments();
                localKey.setStartDate(argments.getLong("startTime"));
                localKey.setEndDate(argments.getLong("endTime"));
                lockAPI.addPeriodKeyboardPassword(extendedBluetoothDevice, argments.getString("pwd"), localKey);
                break;
//                case SET_DELETE_PASSWORD://删除码
//                    mTTTTLockAPI.setDeletePassword(extendedBluetoothDevice, uid, curKey.getLockVersion(), curKey.getAdminPs(), curKey.getUnlockKey(), curKey.getLockFlagPos(), curKey.getAesKeystr(), bleSession.getPassword());
//                    break;
            case Operation.SET_LOCK_TIME://设置锁时间
                if (localKey == null) return;
                lockAPI.setLockTime(extendedBluetoothDevice, localKey);
                break;
            case Operation.RESET_KEYBOARD_PASSWORD://重置键盘密码
                lockAPI.resetKeyboardPassword(extendedBluetoothDevice, localKey);
                break;
            case Operation.RESET_EKEY://重置电子钥匙 锁标志位+1
                lockAPI.resetEKey(extendedBluetoothDevice, localKey);
                break;
            case Operation.RESET_LOCK://重置锁
                if (localKey == null) return;
                lockAPI.resetLock(extendedBluetoothDevice, localKey.getOpenid(), localKey.getLockVersion(), localKey.getAdminPwd(), localKey.getLockKey(), localKey.getLockFlagPos(), localKey.getAesKeystr());
                break;
            case Operation.ADD_IC_CARD:
                if (localKey == null) return;
                lockAPI.addICCard(extendedBluetoothDevice, localKey);
                break;
            case Operation.DELETE_IC_CARD:
                if (localKey == null) return;
                lockAPI.deleteICCard(extendedBluetoothDevice, MyLockAPI.sBleSession.getArgments().getLong("cardNo"), localKey);
                break;
            case Operation.MODIFY_IC_PERIOD:
                if (localKey == null) return;
                lockAPI.modifyICPeriod(extendedBluetoothDevice, MyLockAPI.sBleSession.getArgments().getLong("cardNo"), localKey);
                break;
            case Operation.SEARCH_IC_NUMBER:
                if (localKey == null) return;
                lockAPI.searchICCard(extendedBluetoothDevice, localKey);
                break;
            case Operation.CLEAR_IC_CARD:
                if (localKey == null) return;
                lockAPI.clearICCard(extendedBluetoothDevice, localKey);
                break;
            case Operation.ADD_FINGERPRINT:
                if (localKey == null) return;
                lockAPI.addFingerPrint(extendedBluetoothDevice, localKey);
                break;
            case Operation.MODIFY_FINGERPRINT_PERIOD:
                if (localKey == null) return;
                lockAPI.modifyFingerPrintPeriod(extendedBluetoothDevice, MyLockAPI.sBleSession.getArgments().getLong("FRNo"), localKey);
                break;
            case Operation.CLEAR_FINGERPRINTS:
                lockAPI.clearFingerPrint(extendedBluetoothDevice, localKey);
                break;
            case Operation.GET_OPERATE_LOG://获取操作日志
                lockAPI.getOperateLog(extendedBluetoothDevice, localKey);
                break;
            default:
                break;
            case Operation.GET_LOCK_TIME://获取锁时间
                lockAPI.getLockTime(extendedBluetoothDevice, localKey);
                break;
//                case DELETE_ONE_KEYBOARDPASSWORD://这里的密码类型传0
//                    mTTTTLockAPI.deleteOneKeyboardPassword(extendedBluetoothDevice, uid, localKey.getLockVersion(), localKey.getAdminPs(), localKey.getUnlockKey(), localKey.getLockFlagPos(), 0, bleSession.getPassword(), localKey.getAesKeystr());
//                    break;
//                case GET_LOCK_VERSION_INFO:
//                    mTTTTLockAPI.readDeviceInfo(extendedBluetoothDevice, localKey.getLockVersion(), localKey.getAesKeystr());
//                    break;
        }

    }

    @Override
    public void onDeviceDisconnected(ExtendedBluetoothDevice extendedBluetoothDevice) {
        L.e("-----------onDeviceDisconnected");
        //断开连接
        Intent intent = new Intent(Constant.ACTION_BLE_DISCONNECTED);
        intent.putExtra(Constant.DEVICE, extendedBluetoothDevice);
        mLocalBroadcastManager.sendBroadcast(intent);
//            if(!operateSuccess) {
//                toast("蓝牙已断开");
//            }
//            LogUtil.d("蓝牙断开", DBG);
    }

    @Override
    public void onGetLockVersion(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, int var3, int var4,
                                 int var5, int var6, Error var7) {
        L.e("-----------onGetLockVersion");
    }

    @Override
    public void onAddAdministrator(ExtendedBluetoothDevice extendedBluetoothDevice, String lockVersionString, String adminPs, String unlockKey, String adminKeyboardPwd, String deletePwd, String pwdInfo, long timestamp, String aesKeystr, int feature, String modelNumber, String hardwareRevision, String firmwareRevision, Error error) {
        L.e("-----------onAddAdministrator");
        addAdmin(extendedBluetoothDevice, lockVersionString, adminPs, unlockKey, adminKeyboardPwd, deletePwd, pwdInfo, timestamp, aesKeystr, feature, modelNumber, hardwareRevision, firmwareRevision, error);
    }

    @Override
    public void onResetEKey(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, Error error) {
        L.e("-----------onResetEKey");
        Intent intent = new Intent(Constant.ACTION_RESET_KEY);
        intent.putExtra(Constant.ERROR_MSG, error);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onSetLockName(ExtendedBluetoothDevice extendedBluetoothDevice, String var2, Error var3) {
        L.e("-----------onSetLockName");
    }

    @Override
    public void onSetAdminKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, String adminCode, Error
            error) {
        L.e("-----------onSetAdminKeyboardPassword" + adminCode);
        Intent intent = new Intent(Constant.ACTION_SET_ADMIN_PWD);
        intent.putExtra(Constant.ERROR_MSG, error);
        intent.putExtra("password", adminCode);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onSetDeletePassword(ExtendedBluetoothDevice extendedBluetoothDevice, String var2, Error var3) {
        L.e("-----------onSetDeletePassword");
    }

    @Override
    public void onUnlock(ExtendedBluetoothDevice extendedBluetoothDevice, int uid, int uniqueid, long lockTime, Error error) {
        L.e("--------------onUnlock");
        Intent intent = new Intent(Constant.ACTION_UNLOCK);
        intent.putExtra(Constant.ERROR_MSG, error);
        mLocalBroadcastManager.sendBroadcast(intent);
        MyLockAPI.getLockAPI().connect(extendedBluetoothDevice, Operation.GET_OPERATE_LOG);
    }

    @Override
    public void onSetLockTime(ExtendedBluetoothDevice extendedBluetoothDevice, Error error) {
        L.e("-----------onSetLockTime");
        Intent intent = new Intent(Constant.ACTION_LOCK_SYNC_TIME);
        intent.putExtra(Constant.ERROR_MSG, error);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onGetLockTime(ExtendedBluetoothDevice extendedBluetoothDevice, long date, Error error) {
        L.e("-----------onGetLockTime");
        Intent intent = new Intent(Constant.ACTION_LOCK_GET_TIME);
        intent.putExtra(Constant.ERROR_MSG, error);
        intent.putExtra("date", date);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onResetKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, String pwdInfo, long timestamp, Error error) {
        L.e("-----------onResetKeyboardPassword");
        Intent intent = new Intent(Constant.ACTION_RESET_PWD);
        intent.putExtra(Constant.ERROR_MSG, error);
        intent.putExtra("pwdInfo", pwdInfo);
        intent.putExtra("timestamp", timestamp);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onSetMaxNumberOfKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, Error
            var3) {
        L.e("-----------onSetMaxNumberOfKeyboardPassword");
    }

    @Override
    public void onResetKeyboardPasswordProgress(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, Error
            var3) {
        L.e("-----------onResetKeyboardPasswordProgress");
    }

    @Override
    public void onResetLock(ExtendedBluetoothDevice extendedBluetoothDevice, Error error) {
        L.e("-----------onResetLock");
        Intent intent = new Intent(Constant.ACTION_RESET_LOCK);
        intent.putExtra(Constant.ERROR_MSG, error);
        mLocalBroadcastManager.sendBroadcast(intent);

    }

    @Override
    public void onAddKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, String customPwd,
                                      long startTime, long endTime, Error error) {
        L.e("-----------onAddKeyboardPassword");
        Intent intent = new Intent(Constant.ACTION_CUSTOM_PWD);
        intent.putExtra(Constant.ERROR_MSG, error);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onModifyKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, String
            var3, String var4, Error var5) {
        L.e("-----------onModifyKeyboardPassword");
    }

    @Override
    public void onDeleteOneKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, String
            var3, Error var4) {
        L.e("-----------onDeleteOneKeyboardPassword");
    }

    @Override
    public void onDeleteAllKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, Error var2) {
        L.e("-----------onDeleteAllKeyboardPassword");
    }

    @Override
    public void onGetOperateLog(ExtendedBluetoothDevice extendedBluetoothDevice, String records, Error error) {
        L.e("-----------onGetOperateLog");
        if (Error.SUCCESS != error) return;
        //根据accessToken和lockmac获取钥匙
        List<LockKey> keys = mControlCenter.getLockKeys();
        if (keys == null || keys.size() == 0) return;
        LockKey localKey = null;
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i) == null) return;
            if (TextUtils.isEmpty(keys.get(i).getAccessToken()) || TextUtils.isEmpty(keys.get(i).getLockMac()))
                continue;
            if (keys.get(i).getAccessToken().equals(mUser.getAccess_token()) && keys.get(i).getLockMac().equals(extendedBluetoothDevice.getAddress())) {
                localKey = keys.get(i);
                break;
            }
        }
        if (localKey == null) return;
        L.e("----------records:" + records + "-->" + error);
//        LockHttpAction.getHttpAction(mContext).uploadLockRecords(localKey.getLockId(), records, null);
    }

    @Override
    public void onSearchDeviceFeature(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, int var3, Error
            var4) {
        L.e("-----------onSearchDeviceFeature");
    }

    @Override
    public void onAddICCard(ExtendedBluetoothDevice extendedBluetoothDevice, int status, int battery, long cardNo, Error error) {
        //onAddICCard2--9--1036620029
        L.e("-----------onAddICCard" + status + "--" + battery + "--" + cardNo);
        Intent intent = new Intent(Constant.ACTION_LOCK_IC_CARD);
        intent.putExtra("type", Constant.TYPE_ADD_IC_CARD);
        intent.putExtra(Constant.ERROR_MSG, error);
        intent.putExtra("status", status);
        intent.putExtra("cardNo", cardNo);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onModifyICCardPeriod(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, long cardNo, long startDate, long endDate, Error error) {
        L.e("-----------onModifyICCardPeriod" + startDate + "-->" + endDate);
        Intent intent = new Intent(Constant.ACTION_LOCK_IC_CARD);
        intent.putExtra("type", Constant.TYPE_MODIFY_IC_CARD);
        intent.putExtra("cardNo", cardNo);
        intent.putExtra("startDate", startDate);
        intent.putExtra("endDate", endDate);
        intent.putExtra(Constant.ERROR_MSG, error);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onDeleteICCard(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, long cardNo, Error error) {
        L.e("-----------onDeleteICCard");
        Intent intent = new Intent(Constant.ACTION_LOCK_IC_CARD);
        intent.putExtra("type", Constant.TYPE_DELETE_IC_CARD);
        intent.putExtra(Constant.ERROR_MSG, error);
        intent.putExtra("cardNo", cardNo);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onClearICCard(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, Error error) {
        L.e("-----------onClearICCard");
        Intent intent = new Intent(Constant.ACTION_LOCK_IC_CARD);
        intent.putExtra("type", Constant.TYPE_CLEAR_IC_CARD);
        intent.putExtra(Constant.ERROR_MSG, error);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onSetWristbandKeyToLock(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, Error var3) {
        L.e("-----------onSetWristbandKeyToLock");
    }

    @Override
    public void onSetWristbandKeyToDev(Error error) {
        L.e("-----------onSetWristbandKeyToDev");
    }

    @Override
    public void onSetWristbandKeyRssi(Error error) {
        L.e("-----------onSetWristbandKeyRssi");
    }

    @Override
    public void onAddFingerPrint(ExtendedBluetoothDevice extendedBluetoothDevice, int status, int battery, long fingerPrintNo, Error error) {
//        L.e("-----------onAddFingerPrint:" + "status:" + status + "-->fingerPrintNo:" + fingerPrintNo + "error:" + error);
    }

    @Override
    public void onAddFingerPrint(ExtendedBluetoothDevice extendedBluetoothDevice, int status, int bery, long fingerPrintNo, int maxVail, Error error) {
        L.e("-----------onAddFingerPrint:" + "status:" + status + "-->fingerPrintNo:" + fingerPrintNo + "-->maxVail:" + maxVail + "error:" + error);
        Intent intent = new Intent(Constant.ACTION_LOCK_FINGERPRINT);
        intent.putExtra("type", Constant.TYPE_ADD_FINGERPRINT);
        intent.putExtra("FRNo", fingerPrintNo);
        intent.putExtra("status", status);
        intent.putExtra(Constant.ERROR_MSG, error);
        intent.putExtra("maxVail", maxVail);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onFingerPrintCollection(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, Error error) {
    }

    @Override
    public void onFingerPrintCollection(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int vail,
                                        int maxVail, Error error) {
        L.e("-----------onFingerPrintCollection" + vail + "--:" + maxVail);//1--:4
        Intent intent = new Intent(Constant.ACTION_LOCK_FINGERPRINT);
        intent.putExtra("type", Constant.TYPE_COLLECTION_FINGERPRINT);
        intent.putExtra(Constant.ERROR_MSG, error);
        intent.putExtra("vail", vail);
        intent.putExtra("maxVail", maxVail);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onModifyFingerPrintPeriod(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, long FRNo, long startDate, long endDate, Error error) {
        L.e("-----------onModifyFingerPrintPeriod");
        Intent intent = new Intent(Constant.ACTION_LOCK_FINGERPRINT);
        intent.putExtra("type", Constant.TYPE_MODIFY_FINGERPRINT);
        intent.putExtra("FRNo", FRNo);
        intent.putExtra("startDate", startDate);
        intent.putExtra("endDate", endDate);
        intent.putExtra(Constant.ERROR_MSG, error);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onDeleteFingerPrint(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, long FRNo, Error error) {
        L.e("-----------onDeleteFingerPrint");
    }

    @Override
    public void onClearFingerPrint(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, Error error) {
        L.e("-----------onClearFingerPrint");
        Intent intent = new Intent(Constant.ACTION_LOCK_FINGERPRINT);
        intent.putExtra("type", Constant.TYPE_CLEAR_FINGERPRINT);
        intent.putExtra(Constant.ERROR_MSG, error);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onSearchAutoLockTime(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, int var3, int var4,
                                     int var5, Error var6) {
        L.e("-----------onSearchAutoLockTime");
    }

    @Override
    public void onModifyAutoLockTime(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, int var3, Error
            var4) {
        L.e("-----------onModifyAutoLockTime");
    }

    @Override
    public void onReadDeviceInfo(ExtendedBluetoothDevice extendedBluetoothDevice, String var2, String var3, String
            var4, String var5, String var6) {
        L.e("-----------onReadDeviceInfo");
    }

    @Override
    public void onEnterDFUMode(ExtendedBluetoothDevice extendedBluetoothDevice, Error var2) {
        L.e("-----------onEnterDFUMode");
    }

    @Override
    public void onGetLockSwitchState(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, int var3, Error
            var4) {
        L.e("-----------onGetLockSwitchState");
    }

    @Override
    public void onLock(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int uid, int uniqueid, long lockTime, Error error) {
        L.e("-----------onLock");
    }

    @Override
    public void onScreenPasscodeOperate(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, int var3, Error
            var4) {
        L.e("-----------onScreenPasscodeOperate");
    }

    @Override
    public void onRecoveryData(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, Error var3) {
        L.e("-----------onRecoveryData");
    }

    @Override
    public void onSearchICCard(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, String var3, Error var4) {
        L.e("-----------onSearchICCard");
    }

    @Override
    public void onSearchFingerPrint(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, String var3, Error
            var4) {
        L.e("-----------onSearchFingerPrint");
    }

    @Override
    public void onSearchPasscode(ExtendedBluetoothDevice extendedBluetoothDevice, String var2, Error var3) {
        L.e("-----------onSearchPasscode");
    }

    @Override
    public void onSearchPasscodeParam(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, String var3,
                                      long var4, Error var6) {
        L.e("-----------onSearchPasscodeParam");
    }

    @Override
    public void onOperateRemoteUnlockSwitch(ExtendedBluetoothDevice extendedBluetoothDevice, int var2, int var3,
                                            int var4, int var5, Error var6) {
        L.e("-----------onOperateRemoteUnlockSwitch");
    }


    /**
     * 添加管理员
     */
    private void addAdmin(ExtendedBluetoothDevice extendedBluetoothDevice, String lockVersionString, String adminPs, String unlockKey, String adminKeyboardPwd, String deletePwd, String pwdInfo, long timestamp, String aesKeystr, int feature, String modelNumber, String hardwareRevision, String firmwareRevision, final Error error) {
        L.e("--------error:" + error + "--->" + (error == Error.SUCCESS));
        if (error == Error.SUCCESS) {
            LockKey key = new LockKey();
            key.setAccessToken(ControlCenter.getControlCenter().getUserInfo().getAccess_token());
            key.setAdmin(true);
            key.setLockVersion(lockVersionString);
            key.setLockName(extendedBluetoothDevice.getName());
            key.setLockMac(extendedBluetoothDevice.getAddress());
            key.setAdminPwd(adminPs);
            key.setLockKey(unlockKey);
            key.setNoKeyPwd(adminKeyboardPwd);
            key.setDeletePwd(deletePwd);
            key.setPwdInfo(pwdInfo);
            key.setTimestamp(timestamp);
            key.setAesKeystr(aesKeystr);
            key.setSpecialValue(feature);

            //获取当前时区偏移量
            key.setTimezoneRawOffset(TimeZone.getDefault().getOffset(System.currentTimeMillis()));
            key.setModelNumber(modelNumber);
            key.setHardwareRevision(hardwareRevision);
            key.setFirmwareRevision(firmwareRevision);
            T.show( "锁添加成功，正在上传服务端进行初始化操作");
//            LockHttpAction.getHttpAction(mContext).initLock(key, new OnHttpRequestCallback<Boolean>() {
//                @Override
//                public void onFailure(int errorCode) {
//                    L.e("--------error:" + errorCode);
//
//                }
//
//                @Override
//                public void onSuccess(Boolean aBoolean) {
//                    L.e("--------添加成功:");
//                    Intent intent = new Intent(Constant.ACTION_ADD_ADMIN);
//                    intent.putExtra(Constant.ERROR_MSG, error);
//                    mLocalBroadcastManager.sendBroadcast(intent);
//                }
//            });

        } else {
            Intent intent = new Intent(Constant.ACTION_ADD_ADMIN);
            intent.putExtra(Constant.ERROR_MSG, error);
            mLocalBroadcastManager.sendBroadcast(intent);
        }
    }
}