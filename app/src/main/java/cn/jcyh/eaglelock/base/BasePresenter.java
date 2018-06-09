package cn.jcyh.eaglelock.base;

/**
 * Created by jogger on 2018/6/7.
 */
public abstract class BasePresenter<V extends BaseView, M extends BaseModel> implements IPresenter<V, M> {
    protected V mView;
    protected M mModel;

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void attachModel(M model) {
        mModel = model;
    }

    @Override
    public void detachModel() {
        mModel = null;
    }
}
