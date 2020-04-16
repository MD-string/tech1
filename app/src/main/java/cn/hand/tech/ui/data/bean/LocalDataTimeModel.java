package cn.hand.tech.ui.data.bean;

import java.io.Serializable;
import java.util.List;

import cn.hand.tech.bean.WeightDataBean;

/**
 * Created by hand-hitech2 on 2018-04-04.
 */

public class LocalDataTimeModel implements Serializable {
    private String  LocalDataTime;
    private List<WeightDataBean> weightDataBeanList;
    private boolean isChoose;

    public String  getLocalDataTime() {
        return LocalDataTime;
    }

    public void setLocalDataTime(String localDataTime) {
        LocalDataTime = localDataTime;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public List<WeightDataBean> getWeightDataBeanList() {
        return weightDataBeanList;
    }

    public void setWeightDataBeanList(List<WeightDataBean> weightDataBeanList) {
        this.weightDataBeanList = weightDataBeanList;
    }
}
