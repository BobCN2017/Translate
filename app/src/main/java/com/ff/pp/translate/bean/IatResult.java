package com.ff.pp.translate.bean;

import java.util.List;

/**
 * Created by PP on 2017/2/8.
 */

public class IatResult {
    private int sn;
    private boolean ls;
    private int bg;
    private int ed;
    private List<IatMedium> ws;

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public int getEd() {
        return ed;
    }

    public void setEd(int ed) {
        this.ed = ed;
    }

    public boolean isLs() {
        return ls;
    }

    public void setLs(boolean ls) {
        this.ls = ls;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public List<IatMedium> getWs() {
        return ws;
    }

    public void setWs(List<IatMedium> ws) {
        this.ws = ws;
    }
}
