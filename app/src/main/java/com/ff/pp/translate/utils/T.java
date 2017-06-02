package com.ff.pp.translate.utils;

import android.widget.Toast;

import com.ff.pp.translate.Application.MyApplication;

/**
 * Created by PP on 2017/2/23.
 */

public class T {

    private static Toast mToast;
    public static void showTips(String text){
        if (mToast==null){
            mToast= Toast.makeText(MyApplication.getContext(),"", Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }
}
