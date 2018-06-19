package cn.jcyh.eaglelock.function.ui;

import android.content.Intent;
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
import cn.jcyh.eaglelock.function.adapter.ICManageAdapter;
import cn.jcyh.eaglelock.function.contract.ICManageContract;
import cn.jcyh.eaglelock.function.presenter.ICManagePresenter;
import cn.jcyh.eaglelock.function.ui.dialog.HintDialog;
import cn.jcyh.eaglelock.http.bean.LockHttpResult;
import cn.jcyh.locklib.entity.ICCard;

//ic卡管理
public class ICManageActivity extends BaseActivity<ICManagePresenter> implements ICManageContract.View, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener, PopupMenu.OnMenuItemClickListener {
    private final int UPDATE_REQUEST = 0X0A;
    @BindView(R.id.ibtn_menu)
    ImageButton ibtnMenu;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    private ICManageAdapter mAdapter;
    private PopupMenu mMenu;
    private LockKey mLockKey;
    private int mCurrentID;

    @Override
    public int getLayoutId() {
        return R.layout.activity_icmanage;
    }

    @Override
    protected ICManagePresenter createPresenter() {
        mLockKey = getIntent().getParcelableExtra(Constant.LOCK_KEY);
        return new ICManagePresenter(mLockKey);
    }

    @Override
    protected void init() {
        rvContent.setLayoutManager(new MyLinearLayoutManager(this));
        rvContent.addItemDecoration(new DividerLinearItemDecoration(this));
        mAdapter = new ICManageAdapter(null);
        rvContent.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mPresenter.registerReceiver();
    }

    @Override
    protected void loadData() {
        mPresenter.getICDatas(1, 20);
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
                    mMenu.inflate(R.menu.ic_manage_menu);
                    mMenu.setOnMenuItemClickListener(this);
                }
                mMenu.show();
                break;
        }
    }

    @Override
    public int getCardID() {
        return mCurrentID;
    }

    @Override
    public void getICDatasSuccess(LockHttpResult<ICCard> lockKeyHttpResult) {
        mAdapter.setNewData(lockKeyHttpResult.getList());
    }

    @Override
    public void clearICSuccess() {
        loadData();
    }

    @Override
    public void deleteICSuccess() {
        loadData();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        LockKey lockKey = (LockKey) adapter.getItem(position);
//        if (lockKey == null) return;
//        startNewActivity(KeyInfoActivity.class, Constant.LOCK_KEY, lockKey);
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
                            mPresenter.clearIC();
                        }
                    }
                });
                clearHintDialog.show(getSupportFragmentManager(), HintDialog.class.getSimpleName());
                break;
            case R.id.menu_add:
                Intent intent = new Intent(this, AddICActivity.class);
                intent.putExtra(Constant.LOCK_KEY, mLockKey);
                startActivityForResult(intent, UPDATE_REQUEST);
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UPDATE_REQUEST)
            loadData();
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        final ICCard icCard = (ICCard) adapter.getItem(position);
        if (icCard == null) return false;
        mCurrentID = icCard.getCardId();
        final HintDialog hintDialog = new HintDialog();
        hintDialog.setHintContent(getString(R.string.delete_msg));
        hintDialog.setOnHintDialogListener(new HintDialog.OnHintDialogListener() {
            @Override
            public void confirm(boolean isConfirm) {
                hintDialog.dismiss();
                if (isConfirm) {
                    mPresenter.deleteIC(Long.valueOf(icCard.getCardNumber()));
                }
            }
        });
        hintDialog.show(getSupportFragmentManager(), HintDialog.class.getName());
        return false;
    }
}
