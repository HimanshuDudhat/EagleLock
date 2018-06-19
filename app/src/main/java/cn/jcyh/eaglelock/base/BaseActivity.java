package cn.jcyh.eaglelock.base;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.control.ActivityCollector;
import cn.jcyh.eaglelock.http.api.MyLockAPI;
import cn.jcyh.eaglelock.util.L;
import cn.jcyh.eaglelock.util.StatusUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by jogger on 2018/1/10.
 */
@SuppressWarnings("unchecked")
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements IBaseView {
    private static final int STATUS_COLOR = Color.parseColor("#3f000000");
    private ProgressDialog mProgressDialog;
    protected T mPresenter;
    private Unbinder mBind;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getWindow().setBackgroundDrawable(null);
        mBind = ButterKnife.bind(this);
        mPresenter=createPresenter();
        ActivityCollector.addActivity(this);
        //开启沉浸式状态栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (isFullScreen()) {
            StatusUtil.setActivityFullScreen(this);
        } else {
            StatusUtil.immersive(this, immersiveColor());
        }
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        if (isCheckPermission()) {
            checkPermission(Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        init();
        loadData();
    }

    protected abstract T createPresenter();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void init() {
    }

    protected void loadData() {
    }


    /**
     * 是否开启沉浸式状态栏
     */
    public boolean isImmersive() {
        return true;
    }

    /**
     * 状态栏颜色
     */
    public int immersiveColor() {
        return STATUS_COLOR;
    }


    /**
     * 是否全屏
     */
    public boolean isFullScreen() {
        return false;
    }

    public boolean isCheckPermission() {
        return true;
    }

    protected void checkPermission(final String... permissions) {
        //检查权限
        MyLockAPI.getLockAPI().requestBleEnable(this);
        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable subscribe = rxPermissions
                .requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            L.e("用户同意该权限" + permission.name);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            L.e("用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框");
                        } else {
                            L.e("----------用户拒绝了该权限，并且选中『不再询问』");
                        }
                    }
                });
    }

    @Override
    public void showProgressDialog() {
        if (isFinishing() || getSupportFragmentManager() == null)
            return;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.waiting));
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    @Override
    public boolean isDialogShowing() {
        if (isFinishing() || getSupportFragmentManager() == null)
            return false;
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public void showProgressDialog(String message) {
        if (isFinishing() || getSupportFragmentManager() == null)
            return;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    @Override
    public void cancelProgressDialog() {
        if (mProgressDialog == null) return;
        if (mProgressDialog.isShowing())
            mProgressDialog.cancel();
        mProgressDialog = null;
    }

    public void startNewActivity(Class<? extends AppCompatActivity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void startNewActivityForResult(Class<? extends AppCompatActivity> cls, int result) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, result);
    }


    /**
     * 弹出权限提示
     */

    public void startNewActivity(Class cls, String key, Object value) {
        Intent intent = new Intent(this, cls);
        if (value instanceof Integer) {
            intent.putExtra(key, (Integer) value);
        }
        if (value instanceof Integer[]) {
            intent.putExtra(key, (Integer[]) value);
        }
        if (value instanceof String) {
            intent.putExtra(key, (String) value);
        }
        if (value instanceof String[]) {
            intent.putExtra(key, (String[]) value);
        }
        if (value instanceof Boolean) {
            intent.putExtra(key, (Boolean) value);
        }
        if (value instanceof Byte) {
            intent.putExtra(key, (Byte) value);
        }
        if (value instanceof Byte[]) {
            intent.putExtra(key, (Byte[]) value);
        }
        if (value instanceof Serializable) {
            intent.putExtra(key, (Serializable) value);
        }

        if (value instanceof Serializable[]) {
            intent.putExtra(key, (Serializable[]) value);
        }
        if (value instanceof Parcelable) {
            intent.putExtra(key, (Parcelable) value);
        }
        if (value instanceof Parcelable[]) {
            intent.putExtra(key, (Parcelable[]) value);
        }
        if (value instanceof Float[]) {
            intent.putExtra(key, (Float[]) value);
        }
        startActivity(intent);
    }


    public void startNewActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showToast(int resId) {
        cn.jcyh.eaglelock.util.T.show(resId);
    }

    @Override
    public void showToast(String content) {
        cn.jcyh.eaglelock.util.T.show(content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelProgressDialog();
        ActivityCollector.removeActivity(this);
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        mBind.unbind();
    }
}
