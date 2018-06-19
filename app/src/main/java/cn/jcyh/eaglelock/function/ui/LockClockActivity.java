package cn.jcyh.eaglelock.function.ui;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.LockClockContract;
import cn.jcyh.eaglelock.function.presenter.LockClockPresenter;

//锁时间校准
public class LockClockActivity extends BaseActivity<LockClockPresenter> implements LockClockContract.View {
    @BindView(R.id.tv_date)
    TextView tvDate;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lock_clock;
    }

    @Override
    protected LockClockPresenter createPresenter() {
        LockKey lockKey = getIntent().getParcelableExtra(Constant.LOCK_KEY);
        return new LockClockPresenter(lockKey);
    }

    @Override
    protected void init() {
        mPresenter.getLockTime();
    }

    @Override
    public void getLockTimeSuccess(String lockDate) {
        tvDate.setText(lockDate);
    }

    @Override
    public void setLockTimeSuccess() {

    }

    @OnClick({R.id.ibtn_return, R.id.tv_sync_date})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_return:
                finish();
                break;
            case R.id.tv_sync_date:
                mPresenter.setLockTime();
                break;
        }
    }
}
