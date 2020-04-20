package cn.hand.tech.ui.setting.bean;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-02-07.
 */

public class CheckBean implements Serializable {

    private static final long serialVersionUID =1333774374053445L;
    private boolean isDianya;
    private boolean isNetWork;
    private boolean isGps;
    private boolean isCollection;
    private boolean isSensior;

    public boolean isDianya() {
        return isDianya;
    }

    public void setDianya(boolean dianya) {
        isDianya = dianya;
    }

    public boolean isNetWork() {
        return isNetWork;
    }

    public void setNetWork(boolean netWork) {
        isNetWork = netWork;
    }

    public boolean isGps() {
        return isGps;
    }

    public void setGps(boolean gps) {
        isGps = gps;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public boolean isSensior() {
        return isSensior;
    }

    public void setSensior(boolean sensior) {
        isSensior = sensior;
    }
}
