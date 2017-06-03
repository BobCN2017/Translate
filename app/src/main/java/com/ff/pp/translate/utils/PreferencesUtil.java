package com.ff.pp.translate.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.ff.pp.translate.Application.MyApplication;


/**
 * Created by PP on 2017/3/30.
 */

public class PreferencesUtil {
    private static SharedPreferences preferences;

    static {
        preferences= PreferenceManager.getDefaultSharedPreferences(
                MyApplication.getContext());

    }

    public static void putString(String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getString(String key){
        return preferences.getString(key,null);
    }
}
