package cn.jcyh.eaglelock.function.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.constant.Config;
import cn.jcyh.eaglelock.constant.KeyStatus;
import cn.jcyh.eaglelock.entity.LockKey;

/**
 * Created by jogger on 2018/6/15.
 */
public class KeyManageAdapter extends BaseQuickAdapter<LockKey, BaseViewHolder> {
    public KeyManageAdapter(@Nullable List<LockKey> data) {
        super(R.layout.rv_key_manage_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LockKey item) {
        helper.setText(R.id.tv_name, item.getUsername().replace(Config.EAGLEKING, ""));
        helper.setText(R.id.tv_state, KeyStatus.getStatus(item.getKeyStatus()));
        if (item.getEndDate() > 1) {
            helper.setText(R.id.tv_type, SimpleDateFormat.getInstance().format(new Date(item.getStartDate())) + "-" +
                    SimpleDateFormat.getInstance().format(new Date(item.getEndDate())));
        } else if (item.getEndDate() == 1) {
            helper.setText(R.id.tv_type, R.string.once);
        } else {
            helper.setText(R.id.tv_type, R.string.forver);
        }
        switch (item.getKeyStatus()) {
            case KeyStatus.KEY_WAIT_RECEIVED:
                helper.setText(R.id.tv_state, "");
                break;
            case KeyStatus.KEY_FROZEN:
                helper.setText(R.id.tv_state, "已冻结");
                break;
            case KeyStatus.KEY_DELETED:
                helper.setText(R.id.tv_state, "已删除");
                break;
            case KeyStatus.KEY_RESET:
                helper.setText(R.id.tv_state, "已重置");
                break;
        }
    }
}
