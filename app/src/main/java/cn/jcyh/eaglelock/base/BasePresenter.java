package cn.jcyh.eaglelock.base;

/**
 * Created by jogger on 2018/6/7.
 */
public abstract class BasePresenter<V extends IBaseView, M extends BaseModel> implements IPresenter<V, M> {
    protected V mView;
    protected M mModel;

    protected BasePresenter() {
        mModel = attacheModel();
    }

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

}
