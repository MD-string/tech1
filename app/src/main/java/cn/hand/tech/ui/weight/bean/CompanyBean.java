package cn.hand.tech.ui.weight.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.io.Serializable;

/**
 * Created by hand-hitech2 on 2018-05-03.
 */

public class CompanyBean implements Serializable,IPickerViewData {

    /**
     * address : 昆山市
     * cityId : 59bd13a96c8da407601b5d6a
     * cityName : 苏州
     * companyName : 昆山城管
     * email : 1380000000@qq.com
     * id : 59bd13a96c8da407601b5e8e
     * provinceId : 59bd13a96c8da407601b5ea3
     * provinceName : 江苏省
     * tel : 1380000000
     */

    private String address;
    private String cityId;
    private String cityName;
    private String companyName;
    private String email;
    private String id;
    private String provinceId;
    private String provinceName;
    private String tel;
    private String parentId;
    private String parentName;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String getPickerViewText() {
        return companyName;
    }
}
