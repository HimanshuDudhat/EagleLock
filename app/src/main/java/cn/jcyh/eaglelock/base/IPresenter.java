package cn.jcyh.eaglelock.base;

/**
 * Created by jogger on 2018/6/8.
 */
public interface IPresenter<V extends IBaseView, M extends BaseModel> {
    void attachView(V view);

    void detachView();

    void attachModel(M model);

    void detachModel();
}
