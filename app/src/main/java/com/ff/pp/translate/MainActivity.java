package com.ff.pp.translate;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ff.pp.translate.bean.ResultHolder;
import com.ff.pp.translate.utils.Google;
import com.ff.pp.translate.utils.SpeechIat;
import com.ff.pp.translate.utils.SpeechTts;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;


public class MainActivity extends AppCompatActivity {

    private LinearLayout mChinese, mEnglish;
    private EditText mChineseEditText, mEnglishEditText;
    private SpeechTts mTts;
    private SpeechIat mEnglishIat, mChineseIat;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Google.TRANSLATE:
                    ResultHolder holder = (ResultHolder) msg.obj;
                    showResult(holder);
                    break;

                case SpeechIat.RECOGNIZE:
                    showResult((ResultHolder) msg.obj);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initSpeech();
        initTranslateButtonListener();
        initMicrophoneButtonListener();
        initEditTextClickListener();
    }

    private void initEditTextClickListener() {
        mEnglishEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mEnglishEditText.getEditableText().toString()))
                    mTts.startSpeaking(mEnglishEditText.getEditableText().toString(),true);
            }
        });

        mChineseEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mChineseEditText.getEditableText().toString()))
                    mTts.startSpeaking(mChineseEditText.getEditableText().toString(),false);
            }
        });
    }

    private void initTranslateButtonListener() {
        Button chineseTranslate = (Button) mChinese.findViewById(R.id.button_translate);
        Button englishTranslate = (Button) mEnglish.findViewById(R.id.button_translate);

        chineseTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chinese = getInputString(mChineseEditText);
                if (TextUtils.isEmpty(chinese)) return;
                Google.translate(chinese, "en", mhandler);
            }
        });

        englishTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String english = getInputString(mEnglishEditText);
                if (TextUtils.isEmpty(english)) return;
                Google.translate(english, "zh", mhandler);
            }
        });
    }

    private void initMicrophoneButtonListener() {
        Button chineseMicrophone = (Button) mChinese.findViewById(R.id.button_microphone);
        Button englishMicrophone = (Button) mEnglish.findViewById(R.id.button_microphone);

        chineseMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChineseIat.startListening(mhandler);
            }
        });

        englishMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEnglishIat.startListening(mhandler);
            }
        });
    }

    private void initView() {
        mChinese = (LinearLayout) findViewById(R.id.chinese);
        mEnglish = (LinearLayout) findViewById(R.id.english);

        mChineseEditText = (EditText) mChinese.findViewById(R.id.editText);
        mEnglishEditText = (EditText) mEnglish.findViewById(R.id.editText);
    }

    private void initSpeech() {
        mTts = new SpeechTts(this);
        mEnglishIat = new SpeechIat(this, true);
        mChineseIat = new SpeechIat(this, false);

    }

    private String getInputString(EditText input) {
        String str = input.getText().toString();
        if (TextUtils.isEmpty(str)) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }
        input.setText("");
        return str;
    }

    private void showResult(ResultHolder holder) {
        switch (holder.getLanguage()) {
            case "en":
                mEnglishEditText.setText(holder.getResult());
                mTts.startSpeaking(holder.getResult(),true);
                break;
            case "zh":
                mChineseEditText.setText(holder.getResult());
                mTts.startSpeaking(holder.getResult(),false);
                break;

        }
    }
}
