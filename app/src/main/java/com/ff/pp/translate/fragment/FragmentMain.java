package com.ff.pp.translate.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.ff.pp.translate.R;
import com.ff.pp.translate.bean.ResultHolder;
import com.ff.pp.translate.utils.Google;
import com.ff.pp.translate.utils.SpeechIat;
import com.ff.pp.translate.utils.SpeechTts;
import com.ff.pp.translate.utils.T;

/**
 * Created by PP on 2017/6/2.
 */

public class FragmentMain extends Fragment {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        initSpeech();
        initTranslateButtonListener();
        initMicrophoneButtonListener();
        initEditTextClickListener();

        return view;
    }


    private void initEditTextClickListener() {
        mEnglishEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mEnglishEditText.getEditableText().toString()))
                    mTts.startSpeaking(mEnglishEditText.getEditableText().toString(), true);
            }
        });

        mChineseEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mChineseEditText.getEditableText().toString()))
                    mTts.startSpeaking(mChineseEditText.getEditableText().toString(), false);
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

    private void initView(View view) {
        mChinese = (LinearLayout) view.findViewById(R.id.chinese);
        mEnglish = (LinearLayout) view.findViewById(R.id.english);

        mChineseEditText = (EditText) mChinese.findViewById(R.id.editText);
        mEnglishEditText = (EditText) mEnglish.findViewById(R.id.editText);
    }

    private void initSpeech() {
        mTts = new SpeechTts(getActivity());
        mEnglishIat = new SpeechIat(getActivity(), true);
        mChineseIat = new SpeechIat(getActivity(), false);

    }

    private String getInputString(EditText input) {
        String str = input.getText().toString();
        if (TextUtils.isEmpty(str)) {
            T.showTips("输入不能为空");
            return null;
        }
        input.setText("");
        return str;
    }

    private void showResult(ResultHolder holder) {
        switch (holder.getLanguage()) {
            case "en":
                mEnglishEditText.setText(holder.getResult());
                mTts.startSpeaking(holder.getResult(), true);
                break;
            case "zh":
                mChineseEditText.setText(holder.getResult());
                mTts.startSpeaking(holder.getResult(), false);
                break;

        }
    }
}
