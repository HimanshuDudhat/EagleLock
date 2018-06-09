package cn.jcyh.eaglelock.http;

/**
 * Created by jogger on 2018/6/9.
 */
public class LockHttpAction extends BaseHttpAction {
    private static LockHttpAction sHttpAction;

    public static LockHttpAction geHttpAction() {
        if (sHttpAction == null) {
            synchronized (LockHttpAction.class) {
                if (sHttpAction == null)
                    sHttpAction = new LockHttpAction();
            }
        }
        return sHttpAction;
    }

    private LockHttpAction() {
        super();
    }

    @Override
    public IHttpRequest getHttpRequest(RequestService requestService) {
        return new HttpRequestImp(requestService);
    }

    @Override
    public String getBaseUrl() {
        return RequestService.LOCK_URL;
    }

}
