package cn.jcyh.eaglelock.function.ui;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.LockMainContract;
import cn.jcyh.eaglelock.function.presenter.LockMainPresenter;
import cn.jcyh.eaglelock.util.T;
import cn.jcyh.locklib.entity.Error;
import cn.jcyh.locklib.util.DigitUtil;

//锁的控制主页
public class LockMainActivity extends BaseActivity<LockMainPresenter> implements LockMainContract.View {
    @BindView(R.id.cl_user_container)
    ConstraintLayout clUserContainer;
    @BindView(R.id.cl_admin_container)
    ConstraintLayout clAdminContainer;
    @BindView(R.id.iv_lock)
    ImageView ivLock;
    @BindView(R.id.iv_ic_card)
    ImageView ivICCard;
    @BindView(R.id.tv_ic_card)
    TextView tvICCard;
    @BindView(R.id.iv_fingerprint)
    ImageView ivFingerprint;
    @BindView(R.id.tv_fingerprint)
    TextView tvFingerprint;
    private static final int UPDATE_INFO = 0X01;
    private LockKey mLockKey;
    private AnimationDrawable mUnlockAnim;


    @Override
    public int getLayoutId() {
        return R.layout.activity_lock_main;
    }

    @Override
    protected LockMainPresenter createPresenter() {
        mLockKey = getIntent().getParcelableExtra(Constant.LOCK_KEY);
        return new LockMainPresenter(mLockKey);
    }

    @Override
    protected void init() {
        if (!mLockKey.isAdmin() && !"1".equals(mLockKey.getKeyRight())) {
            //不是管理员，且未被授权
            clUserContainer.setVisibility(View.VISIBLE);
        } else {
            clAdminContainer.setVisibility(View.VISIBLE);
        }
        if (DigitUtil.isSupportFingerPrint(mLockKey.getSpecialValue())) {
            ivFingerprint.setVisibility(View.VISIBLE);
            tvFingerprint.setVisibility(View.VISIBLE);
        } else {
            ivFingerprint.setVisibility(View.GONE);
            tvFingerprint.setVisibility(View.GONE);
        }
        if (DigitUtil.isSupportIC(mLockKey.getSpecialValue())) {
            ivICCard.setVisibility(View.VISIBLE);
            tvICCard.setVisibility(View.VISIBLE);
        } else {
            ivICCard.setVisibility(View.GONE);
            tvICCard.setVisibility(View.GONE);
        }
        mPresenter.registerReceiver();
    }

    @OnClick({R.id.ibtn_return, R.id.iv_lock, R.id.iv_send_key, R.id.iv_send_pwd, R.id.iv_key_manage, R.id.iv_pwd_manage, R.id.iv_ic_card, R.id.iv_fingerprint, R.id.iv_record, R.id.iv_setting, R.id.iv_record_user, R.id.iv_setting_user})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_return:
                finish();
                break;
            case R.id.iv_lock:
                if (mUnlockAnim != null && mUnlockAnim.isRunning()) return;
                if (mUnlockAnim == null) {
                    mUnlockAnim = (AnimationDrawable) getResources().getDrawable(
                            R.drawable.anim_unlock);
                }
                ivLock.setBackground(mUnlockAnim);
                mUnlockAnim.start();
                mPresenter.unlock();
                break;
            case R.id.iv_send_key:
                startNewActivity(SendKeyActivity.class, Constant.LOCK_KEY, mLockKey);
                break;
            case R.id.iv_send_pwd:
                startNewActivity(SendPwdActivity.class, Constant.LOCK_KEY, mLockKey);
                break;
            case R.id.iv_key_manage:
                startNewActivity(KeyManageActivity.class, Constant.LOCK_KEY, mLockKey);
                break;
            case R.id.iv_pwd_manage:
                startNewActivity(PwdManageActivity.class, Constant.LOCK_KEY, mLockKey);
                break;
            case R.id.iv_ic_card:
                startNewActivity(ICManageActivity.class, Constant.LOCK_KEY, mLockKey);
                break;
            case R.id.iv_fingerprint:
                startNewActivity(FingerprintActivity.class, Constant.LOCK_KEY, mLockKey);
                break;
            case R.id.iv_record:
            case R.id.iv_record_user:
                startNewActivity(RecordManageActivity.class, Constant.LOCK_KEY, mLockKey);
                break;
            case R.id.iv_setting:
            case R.id.iv_setting_user:
                Intent intent = new Intent(this, SettingActivity.class);
                intent.putExtra(Constant.LOCK_KEY, mLockKey);
                startActivityForResult(intent, UPDATE_INFO);
                break;
        }
    }


    @Override
    public void unLockResult(Error error) {
        if (error.equals(Error.SUCCESS))
            T.showCustom(R.layout.un_lock_succ_toast);
        else
            showToast(error.getDescription());
        if (mUnlockAnim != null && mUnlockAnim.isRunning())
            mUnlockAnim.stop();
        ivLock.setBackgroundResource(R.mipmap.lock_gif_00000);
    }
}
