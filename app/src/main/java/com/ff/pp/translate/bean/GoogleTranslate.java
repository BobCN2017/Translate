package com.ff.pp.translate.bean;

import java.util.List;

/**
 * Created by PP on 2017/5/11.
 */

public class GoogleTranslate {

    /**
     * data : {"translations":[{"translatedText":"晚上好","detectedSourceLanguage":"en"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<TranslationsBean> translations;

        public List<TranslationsBean> getTranslations() {
            return translations;
        }

        public void setTranslations(List<TranslationsBean> translations) {
            this.translations = translations;
        }

        public static class TranslationsBean {
            /**
             * translatedText : 晚上好
             * detectedSourceLanguage : en
             */

            private String translatedText;
            private String detectedSourceLanguage;

            public String getTranslatedText() {
                return translatedText;
            }

            public void setTranslatedText(String translatedText) {
                this.translatedText = translatedText;
            }

            public String getDetectedSourceLanguage() {
                return detectedSourceLanguage;
            }

            public void setDetectedSourceLanguage(String detectedSourceLanguage) {
                this.detectedSourceLanguage = detectedSourceLanguage;
            }
        }
    }
}
