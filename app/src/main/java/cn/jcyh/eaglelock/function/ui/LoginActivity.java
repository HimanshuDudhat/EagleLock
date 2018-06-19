package cn.jcyh.eaglelock.function.ui;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.function.contract.LoginContract;
import cn.jcyh.eaglelock.function.presenter.LoginPresenter;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.cl_login_container)
    ConstraintLayout clLoginContainer;
    @BindView(R.id.cl_regist_container)
    ConstraintLayout clRegistContainer;
    @BindView(R.id.cl_forget_container)
    ConstraintLayout clForgetContainer;
    @BindView(R.id.et_username_login)
    EditText etUserNameLogin;
    @BindView(R.id.et_password_login)
    EditText etPasswordLogin;
    @BindView(R.id.et_username_regist)
    EditText etUserNameRegist;
    @BindView(R.id.et_code_regist)
    EditText etCodeRegist;
    @BindView(R.id.et_password_regist)
    EditText etPasswordRegist;
    @BindView(R.id.cb_remember_me)
    CheckBox cbRememberMe;
    @BindView(R.id.tv_send_code_regist)
    TextView tvSendCodeRegist;
    @BindView(R.id.et_username_forget)
    EditText etUserNameForget;
    @BindView(R.id.et_password_forget)
    EditText etPasswordForget;
    @BindView(R.id.tv_send_code_forget)
    TextView tvSendCodeForget;
    @BindView(R.id.et_code_forget)
    EditText etCodeForget;
    private static final int LOGIN_SHOW = 0X001;
    private static final int REGIST_SHOW = 0X002;
    private static final int REGIST_SUCC_SHOW = 0X003;
    private static final int FORGET_PWD_SHOW = 0X004;
    private int mCurrentShow = LOGIN_SHOW;
    private static int sRegistCodeTime = 0;
    private static int sForgetCodeTime = 0;
    private Timer mTimerRegist;
    private MyCodeTask mRegistCodeTask;
    private Timer mTimerForget;
    private MyCodeTask mForgetCodeTask;
    private MyHandler mHandler;
//    @BindView(R.id.tv_send_code)
//    TextView tvSendCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        mHandler = new MyHandler(this);
        mPresenter.initView();
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }


    @Override
    public void initView(String account, String pwd, boolean isAutoLogin) {
        etUserNameLogin.setText(account);
        etPasswordLogin.setText(pwd);
        cbRememberMe.setChecked(isAutoLogin);
    }

    @Override
    public String getLoginUserName() {
        return etUserNameLogin.getText().toString().trim();
    }

    @Override
    public String getLoginPassword() {
        return etPasswordLogin.getText().toString().trim();
    }

    @Override
    public boolean getIsAutoLogin() {
        return cbRememberMe.isChecked();
    }

    @Override
    public String getRegistUserName() {
        return etUserNameRegist.getText().toString().trim();
    }

    @Override
    public String getRegistPassword() {
        return etPasswordRegist.getText().toString().trim();
    }

    @Override
    public int getRegistCode() {
        int code = -1;
        String codeString = etCodeRegist.getText().toString().trim();
        if (TextUtils.isEmpty(codeString)) {
            showToast(R.string.input_can_not_null);
            return code;
        }
        try {
            code = Integer.parseInt(codeString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            code = -2;
        }
        return code;
    }

    @Override
    public void requestRegistCodeSuccess() {
        //请求验证码成功
        tvSendCodeRegist.setEnabled(false);
        if (mTimerRegist != null) {
            mTimerRegist.cancel();
            mTimerRegist = null;
        }
        if (mRegistCodeTask != null) {
            mRegistCodeTask.cancel();
            mRegistCodeTask = null;
        }
        mTimerRegist = new Timer();
        mRegistCodeTask = new MyCodeTask(REGIST_SHOW);
        mTimerRegist.schedule(mRegistCodeTask, 0, 1000);
    }

    @Override
    public void registSuccess(String registUserName, String registPassword) {
        etUserNameLogin.setText(registUserName);
        etPasswordLogin.setText(registPassword);
        cbRememberMe.setChecked(true);
    }

    @Override
    public String getForgetUserName() {
        return etUserNameForget.getText().toString().trim();
    }

    @Override
    public String getForgetPassword() {
        return etPasswordForget.getText().toString().trim();
    }

    @Override
    public int getForgetCode() {
        int code = -1;
        String codeString = etCodeForget.getText().toString().trim();
        if (TextUtils.isEmpty(codeString)) {
            showToast(R.string.input_can_not_null);
            return code;
        }
        try {
            code = Integer.parseInt(codeString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            code = -2;
        }
        return code;
    }

    @Override
    public void requestForgetCodeSuccess() {
        tvSendCodeForget.setEnabled(false);
        if (mTimerForget != null) {
            mTimerForget.cancel();
            mTimerForget = null;
        }
        if (mForgetCodeTask != null) {
            mForgetCodeTask.cancel();
            mForgetCodeTask = null;
        }
        mTimerForget = new Timer();
        mForgetCodeTask = new MyCodeTask(FORGET_PWD_SHOW);
        mTimerForget.schedule(mForgetCodeTask, 0, 1000);
    }

    @Override
    public void loginSuccess() {
        //登录成功
        startNewActivity(MainActivity.class);
        finish();
    }

    @Override
    public void setBackPasswordSuccess() {
        etUserNameLogin.setText(getForgetUserName());
        etPasswordLogin.setText(getForgetPassword());
        cbRememberMe.setChecked(true);
        setCurrentShow(LOGIN_SHOW);
    }

    @OnCheckedChanged({R.id.cb_remember_me})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_remember_me:
                mPresenter.setAutoLogin(isChecked);
                break;
        }
    }

    @OnClick({R.id.tv_login, R.id.tv_go_regist, R.id.tv_forget_pwd, R.id.tv_login_rediect})
    public void onLoginClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
            case R.id.tv_login_rediect:
                mPresenter.login();
                break;
            case R.id.tv_go_regist:
                setCurrentShow(REGIST_SHOW);
                break;
            case R.id.tv_forget_pwd:
                setCurrentShow(FORGET_PWD_SHOW);
                break;
        }
    }

    @OnClick({R.id.tv_regist, R.id.tv_go_login, R.id.tv_send_code_regist})
    public void onRegistClick(View v) {
        switch (v.getId()) {
            case R.id.tv_regist:
                mPresenter.regist();
                break;
            case R.id.tv_go_login:
                setCurrentShow(LOGIN_SHOW);
                break;
            case R.id.tv_send_code_regist:
                mPresenter.requestRegistCode();
                break;
        }
    }

    @OnClick({R.id.tv_forget, R.id.tv_send_code_forget})
    public void onForgetClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                mPresenter.setBackPassword();
                break;
            case R.id.tv_send_code_forget:
                mPresenter.requestForgetCode();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimerRegist != null) {
            mTimerRegist.cancel();
            mTimerRegist = null;
        }
        if (mRegistCodeTask != null) {
            mRegistCodeTask.cancel();
            mRegistCodeTask = null;
        }
        if (mTimerForget != null) {
            mTimerForget.cancel();
            mTimerForget = null;
        }
        if (mForgetCodeTask != null) {
            mForgetCodeTask.cancel();
            mForgetCodeTask = null;
        }
        sRegistCodeTime = 0;
        sForgetCodeTime = 0;
    }

    public void setCurrentShow(int currentShow) {
        switch (currentShow) {
            case LOGIN_SHOW:
                clRegistContainer.setVisibility(View.GONE);
                clForgetContainer.setVisibility(View.GONE);
                clLoginContainer.setVisibility(View.VISIBLE);
                break;
            case REGIST_SHOW:
                clRegistContainer.setVisibility(View.VISIBLE);
                clLoginContainer.setVisibility(View.GONE);
                break;
            case FORGET_PWD_SHOW:
                clForgetContainer.setVisibility(View.VISIBLE);
                clLoginContainer.setVisibility(View.GONE);
                break;
            case REGIST_SUCC_SHOW:
                clRegistContainer.setVisibility(View.GONE);
                clLoginContainer.setVisibility(View.VISIBLE);
                break;

        }
        mCurrentShow = currentShow;
    }

    private class MyCodeTask extends TimerTask {
        private int show;

        private MyCodeTask(int show) {
            this.show = show;
        }

        @Override
        public void run() {
            mHandler.sendEmptyMessage(show);
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<LoginActivity> mWeakReference;

        MyHandler(LoginActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = mWeakReference.get();
            if (activity == null || activity.isFinishing() || activity.getSupportFragmentManager() == null)
                return;
            switch (msg.what) {
                case REGIST_SHOW:
                    sRegistCodeTime++;
                    try {
                        if (activity.tvSendCodeRegist != null) {
                            activity.tvSendCodeRegist.setText((60 - sRegistCodeTime) + "s");
                            if (60 - sRegistCodeTime == 0) {
                                if (activity.mTimerRegist != null) {
                                    activity.mTimerRegist.cancel();
                                }
                                if (activity.mRegistCodeTask != null) {
                                    activity.mRegistCodeTask.cancel();
                                }
                                activity.tvSendCodeRegist.setEnabled(true);
                                activity.tvSendCodeRegist.setText(R.string.getcode);
                                sRegistCodeTime = 0;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case FORGET_PWD_SHOW:
                    sForgetCodeTime++;
                    try {
                        if (activity.tvSendCodeForget != null) {
                            activity.tvSendCodeForget.setText((60 - sForgetCodeTime) + "s");
                            if (60 - sForgetCodeTime == 0) {
                                if (activity.mTimerForget != null) {
                                    activity.mTimerForget.cancel();
                                }
                                if (activity.mForgetCodeTask != null) {
                                    activity.mForgetCodeTask.cancel();
                                }
                                activity.tvSendCodeForget.setEnabled(true);
                                activity.tvSendCodeForget.setText(activity.getString(R.string.getcode));
                                sForgetCodeTime = 0;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
