package cn.jcyh.eaglelock.function.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.entity.LockPwdRecord;
import cn.jcyh.locklib.entity.KeyboardPasswdType;

/**
 * Created by jogger on 2018/6/15.
 */
public class PwdManageAdapter extends BaseQuickAdapter<LockPwdRecord, BaseViewHolder> {
    public PwdManageAdapter(@Nullable List<LockPwdRecord> data) {
        super(R.layout.rv_pwd_manage_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LockPwdRecord item) {
        helper.setText(R.id.tv_name, item.getKeyboardPwd());
        Date date = new Date();
        date.setTime(item.getStartDate());
        String startDate = SimpleDateFormat.getInstance().format(date);
        date.setTime(item.getEndDate());
        String endDate = SimpleDateFormat.getInstance().format(date);
        switch (item.getKeyboardPwdType()) {
            case KeyboardPasswdType.ONCE:
                helper.setText(R.id.tv_type, startDate + " " + mContext.getString(R.string.once));
                break;
            case KeyboardPasswdType.PERMENANT:
                helper.setText(R.id.tv_type, startDate + " " + mContext.getString(R.string.forver));
                break;
            case KeyboardPasswdType.PERIOD:
                helper.setText(R.id.tv_type, startDate + " - " + endDate + " " + mContext.getString(R.string.limit_time));
                break;
            default:
                helper.setText(R.id.tv_type, startDate + " - " + endDate + " " + mContext.getString(R.string.custom));
                break;
        }
    }
}
