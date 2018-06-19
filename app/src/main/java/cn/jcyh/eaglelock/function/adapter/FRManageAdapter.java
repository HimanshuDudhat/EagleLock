package cn.jcyh.eaglelock.function.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jcyh.eaglelock.R;
import cn.jcyh.locklib.entity.FR;

/**
 * Created by jogger on 2018/6/15.
 */
public class FRManageAdapter extends BaseQuickAdapter<FR, BaseViewHolder> {
    public FRManageAdapter(@Nullable List<FR> data) {
        super(R.layout.rv_fr_manage_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FR item) {

        helper.setText(R.id.tv_name, item.getFingerprintNumber());
        if (item.getEndDate() > 1) {
            helper.setText(R.id.tv_type, SimpleDateFormat.getInstance().format(new Date(item.getStartDate())) + "-" +
                    SimpleDateFormat.getInstance().format(new Date(item.getEndDate())));
        } else {
            helper.setText(R.id.tv_type, R.string.forver);
        }
    }
}
