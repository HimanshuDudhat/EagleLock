package cn.jcyh.eaglelock.function.ui.dialog;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.jcyh.eaglelock.base.BaseActivity;
import cn.jcyh.eaglelock.base.BaseDialogFragment;


/**
 * Created by jogger on 2017/3/21.
 * 对话框创建
 */

public class DialogHelper {
    public static final int DIALOG_EDIT = 0X001;
    public static final int DIALOG_COMMON_PROGRESS = 0X002;
    public static final int DIALOG_COMMON_HINT = 0X003;//普通提示框
    public static final int DIALOG_CHANGE_PWD = 0X004;
    private BaseDialogFragment mDialogFragment;
    private FragmentManager mFragmentManager;
    private Object mArgment;
    private Object mArgment2;

    private DialogHelper() {
    }

    public DialogHelper(BaseActivity activity, BaseDialogFragment dialogFragment) {
        mFragmentManager = activity.getSupportFragmentManager();
        mDialogFragment = dialogFragment;
    }

    public BaseDialogFragment getDialogFragment() {
        return mDialogFragment;
    }

    public void dismiss() {
        if (mDialogFragment != null && mDialogFragment.getDialog() != null
                && mDialogFragment.getDialog().isShowing()) {
            mDialogFragment.dismiss();
        }
    }

    public void setArgment(Object argment) {
        mArgment = argment;
    }

    public Object getArgment() {
        return mArgment;
    }

    public void setArgment2(Object argment2) {
        mArgment2 = argment2;
    }

    public Object getArgment2() {
        return mArgment2;
    }

    public boolean isShowing() {
        return mDialogFragment != null && mDialogFragment.getDialog() != null &&
                mDialogFragment.getDialog().isShowing();
    }


    public void commit() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(mDialogFragment, mDialogFragment.getClass().getName());
        if (isShowing())
            return;
        transaction.commitAllowingStateLoss();
    }

}
