package com.ff.pp.translate.bean;

/**
 * Created by PP on 2017/7/10.
 */

public class IatStatus {
    //language和accent必须严格按讯飞语音识别文档给值；
    public final String language;  //"en_us" 或者"zh_cn"
    public final String accent;    //null 或者"mandarin" "cantonese" "lms"
    //以下三个变量为语音识别开始、中间、结束时界面上的提示字符串，可自由设置；
    public String tipOfBeginRecognize;
    public String tipOfVolumeChange;
    public String tipOfEndRecognize;

    public IatStatus(String language, String accent, String tipOfBeginRecognize, String tipOfVolumeChange, String tipOfEndRecognize) {
        this.language = language;
        this.accent = accent;
        this.tipOfBeginRecognize = tipOfBeginRecognize;
        this.tipOfVolumeChange = tipOfVolumeChange;
        this.tipOfEndRecognize = tipOfEndRecognize;
    }

    @Override
    public int hashCode() {
        if (accent==null)
            return language.hashCode();
        return language.hashCode()+accent.hashCode();
    }
}
