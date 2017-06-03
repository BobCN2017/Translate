package com.ff.pp.translate.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ff.pp.translate.MainActivity;
import com.ff.pp.translate.R;
import com.ff.pp.translate.bean.Record;
import com.ff.pp.translate.bean.ResultHolder;
import com.ff.pp.translate.utils.Constants;
import com.ff.pp.translate.utils.Google;
import com.ff.pp.translate.utils.PreferencesUtil;
import com.ff.pp.translate.utils.SpeechIat;
import com.ff.pp.translate.utils.SpeechTts;
import com.ff.pp.translate.utils.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PP on 2017/6/2.
 */

public class FragmentMain extends Fragment {
    private static final String TAG = "FragmentMain";
    private CardView mChinese, mEnglish;
    private EditText mChineseEditText, mEnglishEditText;
    private SpeechTts mTts;
    private SpeechIat mEnglishIat, mChineseIat;
    private List<Record> mData;
    private Context mContext;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Google.TRANSLATE:
                    ResultHolder holder = (ResultHolder) msg.obj;
                    saveTranslateRecord(holder);
                    showResult(holder, true);
                    break;

                case SpeechIat.RECOGNIZE:
                    showResult((ResultHolder) msg.obj, false);
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
        initDeleteButtonListener();
        initData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    private void initData() {
        mData = new ArrayList<>();
        List<Record> list = new Gson().fromJson(PreferencesUtil.getString(Constants.RECORD_NAME),
                new TypeToken<List<Record>>() {
                }.getType());
        if (list != null)
            mData.addAll(list);
    }

    private void initTranslateButtonListener() {
        ImageButton chineseTranslate = (ImageButton) mChinese.findViewById(R.id.button_translate);
        ImageButton englishTranslate = (ImageButton) mEnglish.findViewById(R.id.button_translate);

        chineseTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chinese = getInputString(mChineseEditText);
                if (TextUtils.isEmpty(chinese)) return;
                saveInputRecord(chinese, true);
                Google.translate(chinese, "en", mhandler);
            }
        });

        englishTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String english = getInputString(mEnglishEditText);
                if (TextUtils.isEmpty(english)) return;
                saveInputRecord(english, false);
                Google.translate(english, "zh", mhandler);
            }
        });
    }

    private void initDeleteButtonListener() {
        ImageButton chineseDelete = (ImageButton) mChinese.findViewById(R.id.button_delete);
        ImageButton englishDelete = (ImageButton) mEnglish.findViewById(R.id.button_delete);

        chineseDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChineseEditText.setText("");
            }
        });

        englishDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEnglishEditText.setText("");
            }
        });
    }

    private void initMicrophoneButtonListener() {
        ImageButton chineseMicrophone = (ImageButton) mChinese.findViewById(R.id.button_microphone);
        ImageButton englishMicrophone = (ImageButton) mEnglish.findViewById(R.id.button_microphone);

        englishMicrophone.setImageResource(R.drawable.presence_audio_online);

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
        mChinese = (CardView) view.findViewById(R.id.chinese);
        mEnglish = (CardView) view.findViewById(R.id.english);

        mChineseEditText = (EditText) mChinese.findViewById(R.id.editText);
        mEnglishEditText = (EditText) mEnglish.findViewById(R.id.editText);

        mChineseEditText.setHint("请输入中文,点击纸飞机按钮翻译");
        mEnglishEditText.setHint(" Enter in English please,click the bottom right button to translate.");

        ((Button) mEnglish.findViewById(R.id.button_language)).setText("En");
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

    private void showResult(ResultHolder holder, boolean needSpeaking) {
        switch (holder.getLanguage()) {
            case "en":
                mEnglishEditText.append(holder.getResult());
                if (needSpeaking)
                    mTts.startSpeaking(holder.getResult(), true);
                break;
            case "zh":
                mChineseEditText.append(holder.getResult());
                if (needSpeaking)
                    mTts.startSpeaking(holder.getResult(), false);
                break;

        }
    }

    private void saveTranslateRecord(ResultHolder holder) {
        int position;
        if (holder.getLanguage().equals("en"))
            position = Constants.POSITION_UPPER;
        else
            position = Constants.POSITION_UNDER;
        Record record = new Record(holder.getResult(), position, false, System.currentTimeMillis()
                , holder.getLanguage());
        addDataAndSave(record);
    }

    private void addDataAndSave(Record record) {
        mData.add(record);
        String json = new Gson().toJson(mData, new TypeToken<List<Record>>() {
        }.getType());
        PreferencesUtil.putString(Constants.RECORD_NAME, json);
        Log.e(TAG, "addDataAndSave: " + json);
        if (mContext != null)
            ((MainActivity) mContext).updateFragment(1);
    }

    private void saveInputRecord(String content, boolean isChinese) {
        int position;
        String language;
        if (isChinese) {
            position = Constants.POSITION_UPPER;
            language = Constants.Chinese;
        } else {
            position = Constants.POSITION_UNDER;
            language = Constants.English;
        }
        Record record = new Record(content, position, true,
                System.currentTimeMillis(), language);
        addDataAndSave(record);
    }
}
