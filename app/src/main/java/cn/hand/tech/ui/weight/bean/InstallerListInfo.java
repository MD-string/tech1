package cn.hand.tech.ui.weight.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-06-06.
 * 安装人员信息
 */

public class InstallerListInfo implements Serializable{

    private static final long serialVersionUID =1333787433053445L;
    public List<InstallerInfo> list;   //ID

    public List<InstallerInfo> getList() {
        return list;
    }

    public void setList(List<InstallerInfo> list) {
        this.list = list;
    }
}
