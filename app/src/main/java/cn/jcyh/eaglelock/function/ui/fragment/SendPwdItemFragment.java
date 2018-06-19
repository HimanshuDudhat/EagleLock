package cn.jcyh.eaglelock.function.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseFragment;
import cn.jcyh.eaglelock.constant.Constant;
import cn.jcyh.eaglelock.entity.LockKey;
import cn.jcyh.eaglelock.entity.LockKeyboardPwd;
import cn.jcyh.eaglelock.function.contract.SendPwdItemContract;
import cn.jcyh.eaglelock.function.presenter.SendPwdItemPresenter;
import cn.jcyh.eaglelock.function.ui.dialog.CommonEditDialog;
import cn.jcyh.eaglelock.util.L;
import cn.jcyh.locklib.entity.KeyboardPasswdType;

/**
 * Created by jogger on 2018/6/13.发送密码方式
 */
public class SendPwdItemFragment extends BaseFragment<SendPwdItemPresenter> implements SendPwdItemContract.View {
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;

    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.rl_repeat)
    RelativeLayout rlRepeat;
    @BindView(R.id.rl_create_pwd)
    RelativeLayout rlCreatePwd;
    @BindView(R.id.tv_repeat)
    TextView tvRepeat;
    @BindView(R.id.tv_pwd)
    TextView tvPwd;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_create_pwd)
    TextView tvCreatePwd;
    private TimePickerView mStartPickerView, mEndPickerView;
    private static final int FORVER = 0;
    private static final int LIMIT_TIME = 1;
    private static final int ONCE = 2;
    private static final int CLEAR = 3;
    private static final int CUSTOM = 4;
    private static final int REPEAT = 5;
    private long mStartTime, mEndTime;
    private int mPostion;
    private String mCustomPwd;
    private int mKeyboardPwdType;
    private int mCurrentRepeat;
    private AlertDialog mAlertDialog;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_send_pwd;
    }

    @Override
    public void init() {
        mStartTime = System.currentTimeMillis();
        mEndTime = mStartTime + 1000 * 60 * 60;
        Date date = new Date(mStartTime);
        tvStartTime.setText(SimpleDateFormat.getInstance().format(date));
        date.setTime(mEndTime);
        tvRepeat.setText(getResources().getStringArray(R.array.week)[0]);
        tvEndTime.setText(SimpleDateFormat.getInstance().format(date));
        //时间选择器
        TimePickerBuilder startBuilder = new TimePickerBuilder(mActivity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String startTime = SimpleDateFormat.getInstance().format(date);
                L.e("---------->format" + startTime);
                mStartTime = date.getTime();
                tvStartTime.setText(startTime);

            }
        });
        TimePickerBuilder endBuilder = new TimePickerBuilder(mActivity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String startTime = SimpleDateFormat.getInstance().format(date);
                L.e("---------->format" + startTime);
                mEndTime = date.getTime();
                tvEndTime.setText(startTime);
            }
        });
        switch (mPostion) {
            case FORVER://永久
                rlCreatePwd.setVisibility(View.VISIBLE);
                break;
            case LIMIT_TIME://限时
                llTime.setVisibility(View.VISIBLE);
                rlCreatePwd.setVisibility(View.VISIBLE);
                startBuilder.setType(new boolean[]{true, true, true, true, false, false});
                endBuilder.setType(new boolean[]{true, true, true, true, false, false});
                break;
            case ONCE://单次
                rlCreatePwd.setVisibility(View.VISIBLE);
                tvHint.setText("密码有效期为6小时，只能使用1次");
                break;
            case CLEAR://清空
                startBuilder.setType(new boolean[]{true, true, true, true, false, false});
                endBuilder.setType(new boolean[]{true, true, true, true, false, false});
                rlCreatePwd.setVisibility(View.VISIBLE);
                break;
            case CUSTOM://自定义
                llTime.setVisibility(View.VISIBLE);
                rlCreatePwd.setVisibility(View.VISIBLE);
                startBuilder.setType(new boolean[]{true, true, true, true, true, false});
                endBuilder.setType(new boolean[]{true, true, true, true, true, false});
                tvCreatePwd.setText("设置密码");
                break;
            case REPEAT://循环
                startBuilder.setType(new boolean[]{false, false, false, true, false, false});
                endBuilder.setType(new boolean[]{false, false, false, true, false, false});
                llTime.setVisibility(View.VISIBLE);
                rlCreatePwd.setVisibility(View.VISIBLE);
                rlRepeat.setVisibility(View.VISIBLE);
                break;
        }
        mStartPickerView = startBuilder.build();
        mEndPickerView = endBuilder.build();
        mPresenter.registerReceiver();
    }

    @Override
    protected SendPwdItemPresenter createPresenter() {
        Bundle arguments = getArguments();
        assert arguments != null;
        LockKey lockKey = arguments.getParcelable(Constant.LOCK_KEY);
        mPostion = arguments.getInt(Constant.POSITION, 0);
       return new SendPwdItemPresenter(lockKey);
    }

    @OnClick({R.id.tv_create_pwd, R.id.rl_start_time, R.id.rl_end_time, R.id.rl_repeat})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_create_pwd:
                createPwd();
                break;
            case R.id.rl_start_time:
                mStartPickerView.show();
                break;
            case R.id.rl_end_time:
                mEndPickerView.show();
                break;
            case R.id.rl_repeat:
                showRepeatDialog();
                break;
        }
    }

    private void showRepeatDialog() {
        if (mAlertDialog == null) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_repeat_layout, null);
            final WheelView wvDate = view.findViewById(R.id.wv_date);
            wvDate.setCyclic(false);
            List<String> weeks = new ArrayList<>();
            final String[] array = getResources().getStringArray(R.array.week);
            Collections.addAll(weeks, array);
            wvDate.setAdapter(new ArrayWheelAdapter(weeks));
            mAlertDialog = new AlertDialog.Builder(mActivity)
                    .setView(view)
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                        }
                    }).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCurrentRepeat = wvDate.getCurrentItem();
                            mAlertDialog.dismiss();
                            tvRepeat.setText(array[mCurrentRepeat]);
                        }
                    }).create();
        }
        mAlertDialog.show();
    }

    @Override
    public String getKeyboardPwd() {
        return mCustomPwd;
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
    public void customPwdSuccess() {
        tvPwd.setText(mCustomPwd);
    }

    @Override
    public void getPwdSuccess(LockKeyboardPwd lockKeyboardPwd) {
        tvPwd.setText(lockKeyboardPwd.getKeyboardPwd());
    }


    private void createPwd() {
        if (mPostion == CUSTOM) {
            final CommonEditDialog commonEditDialog = new CommonEditDialog();
            commonEditDialog.setOnDialogListener(new CommonEditDialog.OnCommonEditDialogListener() {
                @Override
                public void onConfirm(boolean isConfirm) {
                    commonEditDialog.dismiss();
                    if (isConfirm) {
                        //先传给锁
                        showProgressDialog();
                        mCustomPwd = commonEditDialog.getEditText();
                        mPresenter.addPeriodKeyboardPassword(mCustomPwd, mStartTime, mEndTime);
                    }
                }
            });
            commonEditDialog.show(getChildFragmentManager(), CommonEditDialog.class.getName());
            return;
        }
        L.e("-----------生成密码");
        switch (mPostion) {
            case 0:
                mKeyboardPwdType = KeyboardPasswdType.PERMENANT;//永久
                break;
            case 1:
                mKeyboardPwdType = KeyboardPasswdType.PERIOD;//限时
                break;
            case 2:
                mKeyboardPwdType = KeyboardPasswdType.ONCE;//单次
                break;
            case 3:
                mKeyboardPwdType = KeyboardPasswdType.DELETE;
                break;
            case 5:
                mKeyboardPwdType = mCurrentRepeat + 5;
                break;
        }
        mPresenter.getPwd(mKeyboardPwdType, mStartTime, mEndTime);
    }

}
