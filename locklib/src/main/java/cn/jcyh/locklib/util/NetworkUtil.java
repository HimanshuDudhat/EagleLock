//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.jcyh.locklib.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkUtil {
    public NetworkUtil() {
    }

    public static String getWifiSSid(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String ssid = "";
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                ssid = wifiInfo.getSSID();
                if (ssid.length() > 2 && ssid.charAt(0) == 34 && ssid.charAt(ssid.length() - 1) == 34) {
                    ssid = ssid.substring(1, ssid.length() - 1);
                }
            }
        }

        return ssid;
    }

    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean flag = false;
        if (connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null) {
            flag = connectivityManager.getActiveNetworkInfo().isConnected();
        }

        return flag;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }
}
