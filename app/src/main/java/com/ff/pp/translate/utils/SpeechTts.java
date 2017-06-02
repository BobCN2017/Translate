package com.ff.pp.translate.utils;

import android.content.Context;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;

/**
 * Created by PP on 2017/6/1.
 */

public class SpeechTts {
    private boolean mTtsPause;
    private SpeechSynthesizer mTts;

    public SpeechTts(Context context) {
        mTts = SpeechSynthesizer.createSynthesizer(context, mInitListener);
    }

    public void setupTts() {

        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan"); // 设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "80");// 设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围 0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
    }

    public void startSpeaking(String content, boolean isEnglish) {
        if (isEnglish)
            mTts.setParameter(SpeechConstant.VOICE_NAME, "Catherine");
        else
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.startSpeaking(content, null);
    }

    public void pauseOrResumeTts() {
        if (mTts != null && mTts.isSpeaking() && !mTtsPause) {
            mTts.pauseSpeaking();
            mTtsPause = true;
        } else if (mTts != null && mTts.isSpeaking() && mTtsPause) {
            mTts.resumeSpeaking();
            mTtsPause = false;
        }
    }

    // 初始化监听者；
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code == ErrorCode.SUCCESS) {
                setupTts();
            }
        }
    };


}
