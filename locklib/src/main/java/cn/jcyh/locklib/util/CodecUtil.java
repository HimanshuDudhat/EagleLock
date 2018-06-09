//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.jcyh.locklib.util;

import android.util.Log;

public class CodecUtil {
    public CodecUtil() {
    }

    public static native byte[] encode(byte[] var0);

    public static native byte[] decode(byte[] var0);

    public static native byte[] encodeWithEncrypt(byte[] var0, byte var1);

    public static native byte[] decodeWithEncrypt(byte[] var0, byte var1);

    public static native byte crccompute(byte[] var0);

    public static native String stringFromJNI();

    static {
        Log.i("CodecUtils", "初始化本地jni代码");
        System.loadLibrary("LockCore");
    }
}
