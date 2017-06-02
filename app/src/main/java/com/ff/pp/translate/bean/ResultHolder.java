package com.ff.pp.translate.bean;

/**
 * Created by PP on 2017/5/31.
 */

public class ResultHolder {
    String result;
    String language;

    public ResultHolder(String result, String language) {
        this.result = result;
        this.language = language;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
