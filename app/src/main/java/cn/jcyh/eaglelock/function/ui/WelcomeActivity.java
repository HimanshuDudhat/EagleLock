package cn.jcyh.eaglelock.function.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.base.BasePresenter;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.function.ui.dialog.HintDialog;
import cn.jcyh.eaglelock.util.L;
import cn.jcyh.eaglelock.util.SPUtil;
import cn.jcyh.eaglelock.util.T;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WelcomeActivity extends BaseActivity {
    private static final int REQUEST_CODE_WRITE_SETTINGS = 1;


    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                final HintDialog hintDialogFragmemt = new HintDialog();
                hintDialogFragmemt.setHintContent(getString(R.string.use_system_permission_msg));
                hintDialogFragmemt.setOnHintDialogListener(new HintDialog.OnHintDialogListener() {
                    @Override
                    public void confirm(boolean isConfirm) {
                        hintDialogFragmemt.dismiss();
                        if (isConfirm) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
                        } else {
                            finish();
                        }
                    }
                });
                hintDialogFragmemt.show(getSupportFragmentManager(), "HintDialogFragmemt");
            } else {
                checkPermission();
            }
        } else {
            checkPermission();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Settings.System.canWrite(this)) {
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                T.show(R.string.authorize_failure);
                finish();
            }
        }
    }

    private void checkPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable subscribe = rxPermissions
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            L.e("用户同意该权限" + permission.name);
//                            checkCode()
                            intoMain();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            L.e("用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框");
                        } else {
                            L.e("----------用户拒绝了该权限，并且选中『不再询问』");
                        }
                    }
                });
    }

    private void intoMain() {
//        String currentPush = SPUtil.getInstance().getString(ConstantUtil.CURRENT_PUSH, "");
//        if (SystemUtil.SYS_EMUI.equals(currentPush)) {
//            HMSAgent.connect(this, new ConnectHandler() {
//                @Override
//                public void onConnect(int rst) {
//                    if (rst != 0) {
//                        String account = SPUtil.getInstance().getString(ConstantUtil.ACCOUNT, "");
//                        MobclickAgent.reportError(getApplicationContext(),
//                                "当前用户：" + account + ",HMSAgent.connect连接失败" + rst);
//                        JPushInterface.init(getApplicationContext());
//                        SPUtil.getInstance().put(ConstantUtil.CURRENT_PUSH, "");
//                    }
//                }
//            });
//            HMSAgent.Push.getToken(new GetTokenHandler() {
//                @Override
//                public void onResult(int rtnCode, TokenResult tokenResult) {
//                    if (rtnCode != 0) {
//                        String account = SPUtil.getInstance().getString(ConstantUtil.ACCOUNT, "");
//                        MobclickAgent.reportError(getApplicationContext(),
//                                "当前用户：" + account + ",HMSAgent.Push.getToken错误：" + rtnCode);
//                    }
//                    Timber.e("------------>onResult" + rtnCode);
//                }
//            });
//        }


        SPUtil sharePreUtil = SPUtil.getInstance();
        boolean isFirstInto = sharePreUtil.getBoolean(Constant.IS_FIRST_INTO, true);
        if (isFirstInto) {
            //判断是否第一次使用
            startNewActivity(GuideActivity.class);
        } else {
            //判断是否自动登录
            String account = sharePreUtil.getString(Constant.ACCOUNT, "");
            String pwd = sharePreUtil.getString(Constant.PWD, "");
            boolean auto_login = sharePreUtil.getBoolean(Constant.AUTO_LOGIN, false);
            if (auto_login) {
                startNewActivity(MainActivity.class);
            } else {
                startNewActivity(LoginActivity.class);
            }
//            boolean login_gesture = sharePreUtil.getBoolean(Constant.LOGIN_GUESTURE_SWITCH, false);
//            String gesturePwd = sharePreUtil.getString(ConstantUtil.USER_GESTURE_PWD, "");
//            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd) && auto_login) {
//                //未设置手势，直接进入首页
//                if (!login_gesture) {
//                    FbeeControlCenter.sIsLogin = false;
//                    startNewActivity(MainActivity.class);
//                } else {
//                    startNewActivity(LoginActivity.class);
//                }
//            } else {
//                startNewActivity(LoginActivity.class);
//            }
        }
        finish();
    }

    @Override
    public boolean isCheckPermission() {
        return false;
    }
}
