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
import cn.jcyh.eaglelock.function.adapter.KeyManageAdapter;
import cn.jcyh.eaglelock.function.contract.KeyManageContract;
import cn.jcyh.eaglelock.function.presenter.KeyManagePresenter;
import cn.jcyh.eaglelock.function.ui.dialog.HintDialog;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;

//钥匙管理
public class KeyManageActivity extends BaseActivity<KeyManagePresenter> implements KeyManageContract.View, BaseQuickAdapter.OnItemClickListener, PopupMenu.OnMenuItemClickListener {
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    private KeyManageAdapter mAdapter;
    private PopupMenu mMenu;

    @OnClick({R.id.ibtn_return, R.id.ibtn_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_return:
                finish();
                break;
            case R.id.ibtn_menu:
                if (mMenu == null) {
                    mMenu = new PopupMenu(this, ibtnMenu);
                    mMenu.inflate(R.menu.key_manage_menu);
                    mMenu.setOnMenuItemClickListener(this);
                }
                mMenu.show();
                break;
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_key_manage;
    }

    @Override
    protected void init() {
        rvContent.setLayoutManager(new MyLinearLayoutManager(this));
        rvContent.addItemDecoration(new DividerLinearItemDecoration(this));
        mAdapter = new KeyManageAdapter(null);
        rvContent.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mPresenter.registerReceiver();
        mPresenter.getLockKeys(1, 20);
    }

    @Override
    public void getLockKeysSuccess(LockHttpResult<LockKey> lockKeyHttpResult) {
        mAdapter.setNewData(lockKeyHttpResult.getList());
    }

    @Override
    public void resetKeySuccess() {
        mPresenter.getLockKeys(1, 20);
    }

    @Override
    public void clearKeySuccess() {
        mPresenter.getLockKeys(1, 20);
    }

    @Override
    protected KeyManagePresenter createPresenter() {
        LockKey lockKey = getIntent().getParcelableExtra(Constant.LOCK_KEY);
        return new KeyManagePresenter(lockKey);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        LockKey lockKey = (LockKey) adapter.getItem(position);
        if (lockKey == null) return;
        startNewActivity(KeyInfoActivity.class, Constant.LOCK_KEY, lockKey);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                final HintDialog clearHintDialog = new HintDialog();
                clearHintDialog.setHintContent(getString(R.string.clear_msg));
                clearHintDialog.setOnHintDialogListener(new HintDialog.OnHintDialogListener() {
                    @Override
                    public void confirm(boolean isConfirm) {
                        clearHintDialog.dismiss();
                        if (isConfirm) {
                            mPresenter.clearKey();
                        }
                    }
                });
                clearHintDialog.show(getSupportFragmentManager(), HintDialog.class.getSimpleName());
                break;
            case R.id.menu_reset:
                final HintDialog resetHintDialog = new HintDialog();
                resetHintDialog.setHintContent(getString(R.string.reset_key_msg));
                resetHintDialog.setOnHintDialogListener(new HintDialog.OnHintDialogListener() {
                    @Override
                    public void confirm(boolean isConfirm) {
                        resetHintDialog.dismiss();
                        if (isConfirm) {
                            mPresenter.clearKey();
                        }
                    }
                });
                resetHintDialog.show(getSupportFragmentManager(), HintDialog.class.getSimpleName());
                break;
        }
        return false;
    }
}
