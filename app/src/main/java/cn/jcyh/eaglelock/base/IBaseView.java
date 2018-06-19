package cn.jcyh.eaglelock.base;

/**
 * Created by jogger on 2018/6/9.
 */
public interface IBaseView {
    int getLayoutId();

    void showToast(int resId);

    void showToast(String content);

    void showProgressDialog();

    boolean isDialogShowing();

    void cancelProgressDialog();
}
