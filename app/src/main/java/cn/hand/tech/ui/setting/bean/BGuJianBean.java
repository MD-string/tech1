package cn.hand.tech.ui.setting.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-02-07.
 */

public class BGuJianBean implements Serializable {

    private static final long serialVersionUID =1332424053445L;
    private String name;
    private List<GuJianBean> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GuJianBean> getList() {
        return list;
    }

    public void setList(List<GuJianBean> list) {
        this.list = list;
    }

}
