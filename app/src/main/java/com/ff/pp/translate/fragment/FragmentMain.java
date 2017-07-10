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
import com.ff.pp.translate.bean.IatStatus;
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

import cn.sharesdk.onekeyshare.OnekeyShare;

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
        initShareButtonListener();
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
                T.showTips("发送到Google翻译.");
            }
        });

        englishTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String english = getInputString(mEnglishEditText);
                if (TextUtils.isEmpty(english)) return;
                saveInputRecord(english, false);
                Google.translate(english, "zh", mhandler);
                T.showTips("send to Google Translation.");
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

    private void initShareButtonListener() {
        ImageButton chineseShare = (ImageButton) mChinese.findViewById(R.id.button_share);
        ImageButton englishShare = (ImageButton) mEnglish.findViewById(R.id.button_share);

        chineseShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare(mChineseEditText.getEditableText().toString());
            }
        });

        englishShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare(mEnglishEditText.getEditableText().toString());
            }
        });
    }

    private void initView(View view) {
        mChinese = (CardView) view.findViewById(R.id.chinese);
        mEnglish = (CardView) view.findViewById(R.id.english);

        mChineseEditText = (EditText) mChinese.findViewById(R.id.editText);
        mEnglishEditText = (EditText) mEnglish.findViewById(R.id.editText);

        mChineseEditText.setHint("请输入中文,点击纸飞机按钮翻译");
        mEnglishEditText.setHint("Enter in English please.");


    }

    private void initSpeech() {
        mTts = new SpeechTts(getActivity());
        IatStatus english=new IatStatus("en_us",null,"please speak.","volume:","recognize end.");
        mEnglishIat = new SpeechIat(getActivity(), english);
        IatStatus chinese=new IatStatus("zh_cn","mandarin","请说话.","音量:","结束.");
        mChineseIat = new SpeechIat(getActivity(), chinese);

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

    private void addDataAndSave(Record record) {
        mData.add(record);
        String json = new Gson().toJson(mData, new TypeToken<List<Record>>() {
        }.getType());
        PreferencesUtil.putString(Constants.RECORD_NAME, json);
        Log.e(TAG, "addDataAndSave: " + json);
        if (mContext != null)
            ((MainActivity) mContext).updateFragment(1);
    }

    private void showShare(String content) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(getContext());
    }
}
