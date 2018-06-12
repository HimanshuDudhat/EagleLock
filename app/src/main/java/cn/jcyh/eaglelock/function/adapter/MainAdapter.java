package cn.jcyh.eaglelock.function.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.jcyh.eaglelock.R;
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
        helper.setText(R.id.tv_key_name, TextUtils.isEmpty(item.getLockAlias()) ? item.getLockName() : item.getLockAlias());
    }
}
