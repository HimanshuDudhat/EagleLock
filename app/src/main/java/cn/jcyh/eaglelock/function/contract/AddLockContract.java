package cn.jcyh.eaglelock.function.contract;

import cn.jcyh.eaglelock.base.BaseModel;
import cn.jcyh.eaglelock.base.IBaseView;
import cn.jcyh.eaglelock.base.IPresenter;

public interface AddLockContract {
    interface Model extends BaseModel {
    }

    interface View extends IBaseView {
    }

    interface Presenter extends IPresenter<View, Model> {
    }
}
