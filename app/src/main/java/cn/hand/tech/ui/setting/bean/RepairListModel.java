package cn.hand.tech.ui.setting.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-02-07.
 */

public class RepairListModel implements Serializable {

    private static final long serialVersionUID =1333784443344053445L;
    private String name;
    private String companyId;
    private List<CarListModel> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<CarListModel> getChildren() {
        return children;
    }

    public void setChildren(List<CarListModel> children) {
        this.children = children;
    }
}
