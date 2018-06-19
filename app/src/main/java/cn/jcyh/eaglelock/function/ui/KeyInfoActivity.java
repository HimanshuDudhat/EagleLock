package cn.jcyh.eaglelock.function.ui;

import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.constant.KeyStatus;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.KeyInfoContract;
import cn.jcyh.eaglelock.function.presenter.KeyInfoPresenter;
import cn.jcyh.eaglelock.function.ui.dialog.HintDialog;

public class KeyInfoActivity extends BaseActivity<KeyInfoPresenter> implements KeyInfoContract.View, PopupMenu.OnMenuItemClickListener {
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_expiry_date)
    TextView tvExpiryDate;
    @BindView(R.id.tv_received_account)
    TextView tvReceivedAccount;
    @BindView(R.id.tv_sender)
    TextView tvSender;
    @BindView(R.id.tv_send_date)
    TextView tvSendDate;
    private PopupMenu mMenu;
    private LockKey mLockKey;

    @Override
    public int getLayoutId() {
        return R.layout.activity_key_info;
    }

    @Override
    protected KeyInfoPresenter createPresenter() {
        mLockKey = getIntent().getParcelableExtra(Constant.LOCK_KEY);
        return new KeyInfoPresenter(mLockKey);
    }

    @Override
    public void renameSuccess() {

    }

    @Override
    protected void init() {
        super.init();
        Date date = new Date();
        if (!TextUtils.isEmpty(mLockKey.getUsername())) {
            tvName.setText(mLockKey.getUsername());
        }
        if (mLockKey.getEndDate() == 1) {
            tvExpiryDate.setText(R.string.once);
        } else if (mLockKey.getEndDate() == 0) {
            tvExpiryDate.setText(R.string.forver);
        } else {
            date.setTime(mLockKey.getStartDate());
            String startDate = SimpleDateFormat.getInstance().format(date);
            date.setTime(mLockKey.getEndDate());
            String endDate = SimpleDateFormat.getInstance().format(date);
            tvExpiryDate.setText(startDate + "\n" + endDate);
        }
        tvReceivedAccount.setText(mLockKey.getUsername());
        tvSender.setText(mLockKey.getSenderUsername());
        date.setTime(mLockKey.getTimestamp());
        String sendDate = SimpleDateFormat.getInstance().format(date);
        tvSendDate.setText(sendDate);
    }

    @Override
    public void authKeyUserSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void unAuthKeyUserSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void freezeKeySuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void unFreezeKeySuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void deleteKeySuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @OnClick({R.id.ibtn_return, R.id.ibtn_menu, R.id.tv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_return:
                finish();
                break;
            case R.id.ibtn_menu:
                if (mMenu == null) {
                    mMenu = new PopupMenu(this, ibtnMenu);
                    mMenu.inflate(R.menu.key_info_menu);
                    MenuItem authItem = mMenu.getMenu().findItem(R.id.menu_auth);
                    if ("0".equals(mLockKey.getKeyRight())) {
                        authItem.setTitle(R.string.auth);
                    } else if ("1".equals(mLockKey.getKeyRight())) {
                        authItem.setTitle(R.string.unauth);
                    }
                    MenuItem freezeItem = mMenu.getMenu().findItem(R.id.menu_freeze);
                    if (KeyStatus.KEY_FROZEN.equals(mLockKey.getKeyStatus())) {
                        freezeItem.setTitle(R.string.unfreeze);
                    } else {
                        freezeItem.setTitle(R.string.freeze);
                    }
                    mMenu.setOnMenuItemClickListener(this);
                }
                mMenu.show();
                break;
            case R.id.tv_delete:
                final HintDialog hintDialog = new HintDialog();
                hintDialog.setHintContent(getString(R.string.delete_msg));
                hintDialog.setOnHintDialogListener(new HintDialog.OnHintDialogListener() {
                    @Override
                    public void confirm(boolean isConfirm) {
                        hintDialog.dismiss();
                        if (isConfirm) {
                            mPresenter.deleteKey();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_freeze:
                final HintDialog freezeHintDialog = new HintDialog();
                if (KeyStatus.KEY_FROZEN.equals(mLockKey.getKeyStatus())) {
                    freezeHintDialog.setHintContent(getString(R.string.unfreeze_key_msg));
                } else {
                    freezeHintDialog.setHintContent(getString(R.string.freeze_key_msg));
                }
                freezeHintDialog.setOnHintDialogListener(new HintDialog.OnHintDialogListener() {
                    @Override
                    public void confirm(boolean isConfirm) {
                        freezeHintDialog.dismiss();
                        if (isConfirm) {
                            if (KeyStatus.KEY_FROZEN.equals(mLockKey.getKeyStatus())) {
                                mPresenter.unFreezeKey();
                            } else {
                                mPresenter.freezeKey();
                            }
                        }
                    }
                });
                freezeHintDialog.show(getSupportFragmentManager(), HintDialog.class.getSimpleName());
                break;
            case R.id.menu_auth:
                final HintDialog authHintDialog = new HintDialog();
                authHintDialog.setHintContent(getString(R.string.auth_key_msg));
                if ("0".equals(mLockKey.getKeyRight())) {
                    authHintDialog.setHintContent(getString(R.string.auth_key_msg));
                } else if ("1".equals(mLockKey.getKeyRight())) {
                    authHintDialog.setHintContent(getString(R.string.unauth_key_msg));
                }
                authHintDialog.setOnHintDialogListener(new HintDialog.OnHintDialogListener() {
                    @Override
                    public void confirm(boolean isConfirm) {
                        authHintDialog.dismiss();
                        if ("0".equals(mLockKey.getKeyRight())) {
                            mPresenter.authKeyUser();
                        } else if ("1".equals(mLockKey.getKeyRight())) {
                            mPresenter.unAuthKeyUser();
                        }
                    }
                });
                authHintDialog.show(getSupportFragmentManager(), HintDialog.class.getSimpleName());
                break;
        }
        return false;
    }

}
