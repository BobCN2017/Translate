package com.ff.pp.translate.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.ff.pp.translate.R;

/**
 * Created by PP on 2017/6/3.
 */

public class HttpUtil {
    /**
     * 开新线程测试网络；如果不可用，弹出提示；
     *
     * @param context
     */
    public static void TestNewWork(final Context context) {
        final Handler handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                T.showTips(context.getString(R.string.checkNetwork));
            }

            ;
        };

        new Thread() {
            public void run() {
                if (!isNetworkAvailable(context)) {
                    handler.sendEmptyMessage(0);
                }
            }

            ;
        }.start();

    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context
     * @return true 表示网络可用
     */
    private static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前所连接的网络可用
                return true;
            }
        }
        return false;
    }
}
