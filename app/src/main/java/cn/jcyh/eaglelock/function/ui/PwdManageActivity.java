package cn.jcyh.eaglelock.function.ui;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.base.recyclerview.DividerLinearItemDecoration;
import cn.jcyh.eaglelock.base.recyclerview.MyLinearLayoutManager;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.entity.LockPwdRecord;
import cn.jcyh.eaglelock.function.adapter.PwdManageAdapter;
import cn.jcyh.eaglelock.function.contract.PwdManageContract;
import cn.jcyh.eaglelock.function.presenter.PwdManagePresenter;
import cn.jcyh.eaglelock.function.ui.dialog.HintDialog;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;

//密码管理
public class PwdManageActivity extends BaseActivity<PwdManagePresenter> implements PwdManageContract.View, BaseQuickAdapter.OnItemClickListener, PopupMenu.OnMenuItemClickListener {
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    private LockKey mLockKey;
    private PopupMenu mMenu;
    private PwdManageAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pwd_manage;
    }

    @Override
    protected PwdManagePresenter createPresenter() {
        mLockKey = getIntent().getParcelableExtra(Constant.LOCK_KEY);
        return new PwdManagePresenter(mLockKey);
    }

    @Override
    protected void init() {
        rvContent.setLayoutManager(new MyLinearLayoutManager(this));
        rvContent.addItemDecoration(new DividerLinearItemDecoration(this));
        mAdapter = new PwdManageAdapter(null);
        rvContent.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mPresenter.registerReceiver();
        mPresenter.getPwdRecords(1, 20);
    }

    @Override
    public void getPwdRecordsSuccess(LockHttpResult<LockPwdRecord> lockKeyHttpResult) {
        mAdapter.setNewData(lockKeyHttpResult.getList());
    }

    @Override
    public void resetPwdSuccess() {
        mPresenter.getPwdRecords(1, 20);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        LockPwdRecord lockPwdRecord = (LockPwdRecord) adapter.getItem(position);
        if (lockPwdRecord == null) return;
//        startNewActivity(KeyInfoActivity.class, Constant.LOCK_KEY, lockKey);
        // TODO: 2018/6/19 密码详细页
    }

    @OnClick({R.id.ibtn_return, R.id.ibtn_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_return:
                finish();
                break;
            case R.id.ibtn_menu:
                if (mMenu == null) {
                    mMenu = new PopupMenu(this, ibtnMenu);
                    mMenu.inflate(R.menu.pwd_manage_menu);
                    mMenu.setOnMenuItemClickListener(this);
                }
                mMenu.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                final HintDialog clearHintDialog = new HintDialog();
                clearHintDialog.setHintContent(getString(R.string.reset_pwd_msg));
                clearHintDialog.setOnHintDialogListener(new HintDialog.OnHintDialogListener() {
                    @Override
                    public void confirm(boolean isConfirm) {
                        clearHintDialog.dismiss();
                        if (isConfirm) {
                            mPresenter.resetPwd();
                        }
                    }
                });
                clearHintDialog.show(getSupportFragmentManager(), HintDialog.class.getSimpleName());
                break;
            case R.id.menu_send:
                startNewActivity(SendPwdActivity.class, Constant.LOCK_KEY, mLockKey);
                break;
        }
        return false;
    }
}
