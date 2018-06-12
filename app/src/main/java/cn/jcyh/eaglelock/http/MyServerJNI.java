package cn.jcyh.eaglelock.http;

/**
 * Created by jogger on 2018/6/11.
 */
public class MyServerJNI {
    static {
        System.loadLibrary("my_server");
    }

    public static native String getServerUrl();

    public static native String getServerKey();
}
