package cn.hand.tech.ui.curve.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-06-06.
 * 设备
 */

public class Curvenfo implements Serializable{

    public int  id;
    public List<Float> hlist;
    public String colorStr;
    public boolean isShow;

    public Curvenfo() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Float> getHlist() {
        return hlist;
    }

    public void setHlist(List<Float> hlist) {
        this.hlist = hlist;
    }

    public String getColorStr() {
        return colorStr;
    }

    public void setColorStr(String colorStr) {
        this.colorStr = colorStr;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
