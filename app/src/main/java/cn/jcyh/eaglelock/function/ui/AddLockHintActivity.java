package cn.jcyh.eaglelock.function.ui;

import android.view.View;

import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.base.BasePresenter;

public class AddLockHintActivity extends BaseActivity {


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_lock_hint;
    }

    @OnClick({R.id.ibtn_return, R.id.tv_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_return:
                finish();
                break;
            case R.id.tv_next:
                startNewActivity(AddLockActivity.class);
                break;
        }
    }
}
