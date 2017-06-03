package com.ff.pp.translate.bean;

/**
 * Created by PP on 2017/6/2.
 */

public class Record {

    private String content;
    private int position;
    private boolean input;
    private long time;
    private String language;

    public Record(String content, int position, boolean input, long time) {
        this.content = content;
        this.position = position;
        this.input = input;
        this.time = time;
    }

    public Record(String content, int position, boolean input, long time, String language) {
        this.content = content;
        this.position = position;
        this.input = input;
        this.time = time;
        this.language = language;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
