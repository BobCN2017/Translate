package com.ff.pp.translate.bean;

import java.util.List;

/**
 * Created by PP on 2017/2/8.
 */

public class IatMedium {
    private int bg;

    private List<IatWord> cw;

    public List<IatWord> getCw() {
        return cw;
    }

    public void setCw(List<IatWord> cw) {
        this.cw = cw;
    }

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }
}
