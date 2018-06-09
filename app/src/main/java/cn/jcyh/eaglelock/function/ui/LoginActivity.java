package cn.jcyh.eaglelock.function.ui;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.jcyh.eaglelock.R;
import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.function.contract.LoginContract;
import cn.jcyh.eaglelock.function.presenter.LoginPresenter;
import cn.jcyh.eaglelock.util.T;

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
    @BindView(R.id.et_password_regist)
    EditText etPasswordRegist;
    @BindView(R.id.cb_remember_me)
    CheckBox cbRememberMe;

//    @BindView(R.id.tv_send_code)
//    TextView tvSendCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


//    @OnClick({R.id.tv_send_code})
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_send_code:
//                break;
//        }
//    }

    @Override
    protected void createPresenter() {
        mPresenter = new LoginPresenter();
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
    public String getRegistUserName() {
        return etUserNameRegist.getText().toString().trim();
    }

    @Override
    public String getRegistPassword() {
        return etPasswordRegist.getText().toString().trim();
    }

    @Override
    public void showNoNullToast() {
        T.show(R.string.input_can_not_null);
    }

    @OnCheckedChanged({R.id.cb_remember_me})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_remember_me:
                mPresenter.setAutoLogin(isChecked);
                break;
        }
    }

    @OnClick({R.id.tv_login, R.id.tv_go_regist, R.id.tv_forget_pwd})
    public void onLoginClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                mPresenter.login();
                break;
            case R.id.tv_go_regist:
                break;
            case R.id.tv_forget_pwd:

                break;
        }
    }

    @OnClick({R.id.tv_regist})
    public void onRegistClick(View v) {
        switch (v.getId()) {
            case R.id.tv_regist:
                break;
        }
    }

    @OnClick({R.id.tv_forget})
    public void onForgetClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                break;
        }
    }
}
