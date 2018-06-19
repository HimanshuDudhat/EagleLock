package cn.jcyh.eaglelock.function.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.constant.KeyStatus;
import cn.jcyh.eaglelock.entity.LockKey;

/**
 * Created by jogger on 2018/6/12.
 */
public class MainAdapter extends BaseQuickAdapter<LockKey, BaseViewHolder> {


    public MainAdapter(@Nullable List<LockKey> data) {
        super(R.layout.rv_main_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LockKey item) {
        helper.setText(R.id.tv_power, item.getElectricQuantity() + "%");
        helper.setText(R.id.tv_key_name, TextUtils.isEmpty(item.getLockAlias()) ? item.getLockName() : item.getLockAlias());
        helper.setText(R.id.tv_aging, item.isAdmin() ? R.string.forver : R.string.limit_time);
        if (item.getEndDate() == 0)
            helper.setText(R.id.tv_aging, R.string.forver);
        else if (item.getEndDate() == 1)
            helper.setText(R.id.tv_aging, R.string.once);
        else {
            helper.setText(R.id.tv_aging, R.string.limit_time);
        }
        if (KeyStatus.KEY_FROZEN.equals(item.getKeyStatus())) {
            helper.setBackgroundColor(R.id.cl_item, mContext.getResources().getColor(R.color.gray_b8c4d4));
            helper.getView(R.id.cl_item).setEnabled(false);
            helper.setVisible(R.id.tv_state, true);
            helper.setText(R.id.tv_state, R.string.freezed);
        } else {
            helper.setBackgroundColor(R.id.cl_item, mContext.getResources().getColor(R.color.white));
            helper.getView(R.id.cl_item).setEnabled(true);
            helper.setVisible(R.id.tv_state, false);
        }
        helper.getView(R.id.tv_type).setVisibility(item.isAdmin() ? View.VISIBLE : View.GONE);
    }
}
