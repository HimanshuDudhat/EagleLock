package cn.jcyh.eaglelock.function.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jcyh.eaglelock.R;
import cn.jcyh.locklib.entity.ICCard;

/**
 * Created by jogger on 2018/6/15.
 */
public class ICManageAdapter extends BaseQuickAdapter<ICCard, BaseViewHolder> {
    public ICManageAdapter(@Nullable List<ICCard> data) {
        super(R.layout.rv_ic_manage_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ICCard item) {

        helper.setText(R.id.tv_name, item.getCardNumber());
        if (item.getEndDate() > 1) {
            helper.setText(R.id.tv_type, SimpleDateFormat.getInstance().format(new Date(item.getStartDate())) + "-" +
                    SimpleDateFormat.getInstance().format(new Date(item.getEndDate())));
        } else {
            helper.setText(R.id.tv_type, R.string.forver);
        }
    }
}
