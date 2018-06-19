package cn.jcyh.eaglelock.function.ui;

import android.annotation.SuppressLint;
import android.text.InputType;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.SettingContract;
import cn.jcyh.eaglelock.function.presenter.SettingPresenter;
import cn.jcyh.eaglelock.function.ui.dialog.CommonEditDialog;

// TODO: 2018/6/15 有效期
//设置
public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingContract.View {
    @BindView(R.id.rl_lock_name)
    RelativeLayout rlLockName;
    @BindView(R.id.rl_time_calibration)
    RelativeLayout rlTimeCalibration;
    @BindView(R.id.rl_admin_pwd)
    RelativeLayout rlAdminPwd;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_mac)
    TextView tvMac;
    @BindView(R.id.tv_power)
    TextView tvPower;
    @BindView(R.id.tv_lock_name)
    TextView tvLockName;
    @BindView(R.id.tv_admin_pwd)
    TextView tvAdminPwd;
    @BindView(R.id.tv_date)
    TextView tvDate;
    private LockKey mLockKey;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected SettingPresenter createPresenter() {
        mLockKey = getIntent().getParcelableExtra(Constant.LOCK_KEY);
        return new SettingPresenter(mLockKey);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void init() {
        boolean isAdmin = mLockKey.isAdmin() || "1".equals(mLockKey.getKeyRight());
        rlAdminPwd.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        rlLockName.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        rlAdminPwd.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        tvNumber.setText(mLockKey.getLockName());
        tvLockName.setText(mLockKey.getLockAlias() + "");
        tvMac.setText(mLockKey.getLockMac());
        tvPower.setText(mLockKey.getElectricQuantity() + "");
        tvAdminPwd.setText(mLockKey.getNoKeyPwd());
        if (mLockKey.getEndDate() == 0)
            tvDate.setText(R.string.forver);
        else if (mLockKey.getEndDate() == 1)
            tvDate.setText(R.string.once);
        else {
            Date date = new Date();
            date.setTime(mLockKey.getStartDate());
            String startDate = SimpleDateFormat.getInstance().format(date);
            date.setTime(mLockKey.getEndDate());
            String endDate = SimpleDateFormat.getInstance().format(date);
            tvDate.setText(startDate + "-" + endDate);
        }
    }

    @OnClick({R.id.ibtn_return, R.id.rl_lock_name, R.id.rl_time_calibration, R.id.rl_admin_pwd, R.id.tv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_return:
                finish();
                break;
            case R.id.rl_lock_name:
                rename();
                break;
            case R.id.rl_time_calibration:
                startNewActivity(LockClockActivity.class, Constant.LOCK_KEY, mLockKey);
                break;
            case R.id.rl_admin_pwd:
                adminLockPwd();
                break;
            case R.id.tv_delete:
                mPresenter.deleteLockKey();
                break;
        }
    }

    /**
     * 重命名
     */
    private void rename() {
        final CommonEditDialog commonEditDialog = new CommonEditDialog();
        commonEditDialog.setHintContent(getString(R.string.input_name));
        commonEditDialog.setInputType(InputType.TYPE_CLASS_TEXT);
        commonEditDialog.setOnDialogListener(new CommonEditDialog.OnCommonEditDialogListener() {
            @Override
            public void onConfirm(boolean isConfirm) {
                commonEditDialog.dismiss();
                if (isConfirm) {
                    final String content = commonEditDialog.getEditText();
                    mPresenter.rename(content);
                }
            }
        });
        commonEditDialog.show(getSupportFragmentManager(), CommonEditDialog.class.getName());
    }

    /**
     * 管理员开锁密码
     */
    private void adminLockPwd() {
        final CommonEditDialog commonEditDialog = new CommonEditDialog();
        commonEditDialog.setOnDialogListener(new CommonEditDialog.OnCommonEditDialogListener() {
            @Override
            public void onConfirm(boolean isConfirm) {
                commonEditDialog.dismiss();
                if (isConfirm) {
                    String content = commonEditDialog.getEditText();
                    mPresenter.setAdminKeyboardPassword(content);
                }
            }
        });
        commonEditDialog.show(getSupportFragmentManager(), CommonEditDialog.class.getName());
    }

    @Override
    public void onRenameSuccess(String content) {
        setResult(RESULT_OK);
        tvLockName.setText(content);
    }

    @Override
    public void onSetAdminPwdSuccess(String password) {
        tvAdminPwd.setText(password);
        setResult(RESULT_OK);
    }

    @Override
    public void onDeleteLockKeySuccess() {
        startNewActivity(MainActivity.class);
        finish();
    }
}
