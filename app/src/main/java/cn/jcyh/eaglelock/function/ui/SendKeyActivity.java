package cn.jcyh.eaglelock.function.ui;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.function.contract.SendKeyContract;
import cn.jcyh.eaglelock.function.presenter.SendKeyPresenter;

//发送钥匙
public class SendKeyActivity extends BaseActivity<SendKeyPresenter> implements SendKeyContract.View {
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.et_key_account)
    EditText etKeyAccount;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_single_hint)
    TextView tvSingleHint;
    private static final int LIMIT_TIME = 0;
    private static final int FORVER = 1;
    private TimePickerView mStartPickerView, mEndPickerView;
    private long mStartTime, mEndTime;
    private int mCurrentType;
    private PopupWindow mPopupWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_key;
    }

    @Override
    protected SendKeyPresenter createPresenter() {
        LockKey lockKey = getIntent().getParcelableExtra(Constant.LOCK_KEY);
        return new SendKeyPresenter(lockKey);
    }

    @Override
    protected void init() {
        mStartTime = System.currentTimeMillis();
        mEndTime = mStartTime + 1000 * 60 * 60;
        Date date = new Date(mStartTime);
        tvStartTime.setText(SimpleDateFormat.getInstance().format(date));
        date.setTime(mEndTime);
        tvEndTime.setText(SimpleDateFormat.getInstance().format(date));
        //时间选择器
        mStartPickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String startTime = SimpleDateFormat.getInstance().format(date);
                mStartTime = date.getTime();
                tvStartTime.setText(startTime);

            }
        }).setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .build();
        mEndPickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String endTime = SimpleDateFormat.getInstance().format(date);
                tvEndTime.setText(endTime);
                mEndTime = date.getTime();
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .build();
    }

    @Override
    public long getStartTime() {
        return mStartTime;
    }

    @Override
    public long getEndTime() {
        return mEndTime;
    }

    @Override
    public String getReceiveAccount() {
        return etKeyAccount.getText().toString().trim();
    }

    @Override
    public int getCurrentType() {
        return mCurrentType;
    }

    @Override
    public void sendKeySuccess() {

    }

    @OnClick({R.id.ibtn_return, R.id.rl_key_type, R.id.rl_start_time, R.id.rl_end_time, R.id.tv_send_key})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_return:
                finish();
                break;
            case R.id.rl_key_type:
                chooseKeyType();
                break;
            case R.id.rl_start_time:
                mStartPickerView.show();
                break;
            case R.id.rl_end_time:
                mEndPickerView.show();
                break;
            case R.id.tv_send_key:
                mPresenter.sendKey();
                break;
        }
    }

    private void chooseKeyType() {
        if (mPopupWindow != null) {
            if (mPopupWindow.isShowing())
                mPopupWindow.dismiss();
            else {
                mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                backgroundAlpha(0.5f);
            }
            return;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.window_key_type_layout, null);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        view.findViewById(R.id.tv_limit_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentType = 0;
                mPopupWindow.dismiss();
            }
        });
        view.findViewById(R.id.tv_forever).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentType = 1;
                mPopupWindow.dismiss();
            }
        });
        view.findViewById(R.id.tv_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentType = 2;
                mPopupWindow.dismiss();
            }
        });
        backgroundAlpha(0.5f);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
                if (mCurrentType == LIMIT_TIME) {
                    llTime.setVisibility(View.VISIBLE);
                    tvSingleHint.setVisibility(View.GONE);
                    tvType.setText(R.string.limit_time);
                } else {
                    llTime.setVisibility(View.GONE);
                    if (mCurrentType == FORVER) {
                        tvType.setText(R.string.forver);
                        tvSingleHint.setVisibility(View.GONE);
                    } else {
                        tvType.setText(R.string.once);
                        tvSingleHint.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mStartPickerView != null && mStartPickerView.isShowing())
            mStartPickerView.dismiss();
        if (mEndPickerView != null && mEndPickerView.isShowing())
            mEndPickerView.dismiss();
    }
}
