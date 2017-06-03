package com.ff.pp.translate.utils;

import android.os.Handler;
import android.util.Log;

import com.ff.pp.translate.bean.GoogleTranslate;
import com.ff.pp.translate.bean.ResultHolder;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



/**
 * Created by PP on 2017/5/31.
 */

public class Google {

    private static final String TAG = "Google";
    public static final int TRANSLATE = 100;
    private static final String URL_GOOLGE_TRANS = "https://translation.googleapis.com/language/" +
            "translate/v2?key=AIzaSyBp0BGNKYJhjzHzXvcUgH1_iowaSyFL5fM";


    public static void translate(final String string, final String language, final Handler handler) {

        FormBody body = new FormBody.Builder()
                .add("target", language)
                .add("q", string)
                .build();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(body)
                .url(URL_GOOLGE_TRANS)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.showTips("服务器连接故障，请稍后再试.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();
                Log.e(TAG, "onResponse: "+str );
                GoogleTranslate translate = new Gson().fromJson(str, GoogleTranslate.class);
                String result = translate.getData().getTranslations().get(0).getTranslatedText();
                ResultHolder holder=new ResultHolder(result,language);
                handler.obtainMessage(TRANSLATE,holder).sendToTarget();
            }
        });
    }
}
