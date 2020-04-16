package cn.hand.tech.ui.weight.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-04-04.
 */

public class XlsDataTimeModel implements Serializable {
    private String  LocalDataTime;
    private List<XlsData> weightDataBeanList;

    public String  getLocalDataTime() {
        return LocalDataTime;
    }

    public void setLocalDataTime(String localDataTime) {
        LocalDataTime = localDataTime;
    }

    public List<XlsData> getWeightDataBeanList() {
        return weightDataBeanList;
    }

    public void setWeightDataBeanList(List<XlsData> weightDataBeanList) {
        this.weightDataBeanList = weightDataBeanList;
    }
}
