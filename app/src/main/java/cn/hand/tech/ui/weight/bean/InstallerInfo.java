package cn.hand.tech.ui.weight.bean;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-06-06.
 * 安装人员信息
 */

public class InstallerInfo implements Serializable{

    private static final long serialVersionUID =1333787433053445L;
    public String id;   //ID
    public String userName; //
    public String name; //
    public String  createTime;
    public String  description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
