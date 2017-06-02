package com.ff.pp.translate.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.ff.pp.translate.bean.IatMedium;
import com.ff.pp.translate.bean.IatResult;
import com.ff.pp.translate.bean.IatWord;
import com.ff.pp.translate.bean.ResultHolder;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Created by PP on 2017/6/1.
 */

public class SpeechIat {
    private static final String TAG = "SpeechIat";
    public static final int RECOGNIZE = 101;
    private SpeechRecognizer mIats;
    private Gson gson;
    private boolean isEnglish;
    private Handler mHandler;
    // 语音识别初始化监听者；
    private InitListener mIatInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code == ErrorCode.SUCCESS) {
                if (isEnglish)
                    setIatParamsAsEnglish();
                else
                    setIatParamsAsChinese();
            } else {
            }
        }
    };

    public SpeechIat(Context context, boolean isEnglish) {
        // 语音输入初始化；
        if (isEnglish)
            mIats = SpeechRecognizer.createRecognizer(context, mIatInitListener);
        else
            mIats=getRecognizerByReflect(context,mIatInitListener);
        gson = new Gson();
        this.isEnglish = isEnglish;
    }

    private SpeechRecognizer getRecognizerByReflect(Context context, InitListener mIatInitListener) {
        SpeechRecognizer recognizer=null;
        Class<SpeechRecognizer> cl = SpeechRecognizer.class;
        try {
            Constructor<SpeechRecognizer> constructor = cl.getDeclaredConstructor(Context.class, InitListener.class);
            constructor.setAccessible(true);
            recognizer=constructor.newInstance(context,mIatInitListener);
            Log.e(TAG, "getRecognizerByReflect: " );
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "getRecognizerByReflect NoSuchMethodException: " );
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "getRecognizerByReflect IllegalAccessException: " );
            e.printStackTrace();
        } catch (InstantiationException e) {
            Log.e(TAG, "getRecognizerByReflect InstantiationException: " );
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.e(TAG, "getRecognizerByReflect InvocationTargetException: " );
            e.printStackTrace();
        }

        return recognizer;
    }

    public void setIatParamsAsEnglish() {
        setIatParams();
        mIats.setParameter(SpeechConstant.LANGUAGE, "en_us");
        isEnglish = true;
    }

    public void setIatParamsAsChinese() {
        setIatParams();
        mIats.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIats.setParameter(SpeechConstant.ACCENT, "mandarin");
        isEnglish = false;
    }

    // 设置语音识别参数；
    private void setIatParams() {
        //设置前清空参数；
        mIats.setParameter(SpeechConstant.PARAMS, null);
        //设置新参数；
        mIats.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mIats.setParameter(SpeechConstant.RESULT_TYPE, "json");

        mIats.setParameter(SpeechConstant.DOMAIN, "iat");

        mIats.setParameter(SpeechConstant.VAD_BOS, "4000");
        mIats.setParameter(SpeechConstant.VAD_EOS, "1000");
        mIats.setParameter(SpeechConstant.ASR_PTT, "true");
        Log.e(TAG, "setIatParams: finish");
    }

    private RecognizerListener mRecognizeListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            if (isEnglish) {
                T.showTips("volume:" + i);
            } else {
                T.showTips("音量:" + i);
            }
        }

        @Override
        public void onBeginOfSpeech() {
            if (isEnglish) {
                T.showTips("please speak");
            } else {
                T.showTips("请说话");
            }

        }

        @Override
        public void onEndOfSpeech() {
            if (isEnglish) {
                T.showTips("end.");
            } else {
                T.showTips("结束.");
            }
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            if (isLast) // 抛弃最后一句；
                return;
            IatResult iatResult = gson.fromJson(results.getResultString(), IatResult.class);
            StringBuilder builder = new StringBuilder();
            List<IatMedium> ws = iatResult.getWs();
            if (ws != null) {
                for (int i = 0; i < ws.size(); i++) {
                    List<IatWord> cw = ws.get(i).getCw();
                    if (cw != null) {
                        for (int j = 0; j < cw.size(); j++) {
                            builder.append(cw.get(j).getW());
                        }
                    }
                }
            }
            String text = builder.toString().trim();
            ResultHolder holder;
            if (isEnglish) {
                holder = new ResultHolder(text, "en");
            } else {
                holder = new ResultHolder(text, "zh");
            }
            mHandler.obtainMessage(RECOGNIZE, holder).sendToTarget();
        }

        @Override
        public void onError(SpeechError speechError) {
            int size = speechError.getPlainDescription(true).length() - 11;
            T.showTips(speechError.getPlainDescription(true).substring(0, size));
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            Log.e(TAG, "onEvent: ");
        }
    };

    public void startListening(Handler handler) {
        mHandler = handler;
        mIats.startListening(mRecognizeListener);
        Log.e(TAG, "startListening mRecognizeListener: " + mRecognizeListener.toString());
    }
}
