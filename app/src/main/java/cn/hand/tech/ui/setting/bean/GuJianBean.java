package cn.hand.tech.ui.setting.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hand-hitech2 on 2018-02-07.
 */

public class GuJianBean implements Serializable {

    private static final long serialVersionUID =13332444374053445L;
    /**
     *固件id
     */
    private String id;
    /**
     *上传用户
     */
    private String userId;
    /**
     *上传用户
     */
    private int m_userId;
    /**
     *Bin文件名称
     */
    private String binName;
    /**
     *Bin文件大小
     */
    private String binSize;
    /**
     *上传时间
     */
    private String uploadDate;
    /**
     *Bin文件字节数组
     */
    private List<String> binBytesStr;

    /**
     *Bin文件状态1：正常2：冻结3：删除
     */
    private String status;
    /**
     *Bin文件描述
     */
    private String description;
    /**
     *版本信息
     */
    private String version;
    /**
     *存储路径
     */
    private String binPath;
    /**
     *Bin文件MD5
     */
    private String MD5;
    /**
     *Bin文件总数据包个数计算方式：packetTotal=binSize/packetSize（向上取整）;
     */
    private int packetTotal;
    /**
     *数据包大小默认为1024
     */
    private int packetSize;
    /**
     *协议版本号
     */
    private String protocolVersion;
    /**
     *协议版本号
     */
    private String hwVersion;
    /**
     *绑定公司id
     */
    private String companyId;
    /**
     *公司名称
     */
    private String companyName;

    /**
     *bin 包 数据 str
     */
    private String binStr;

    public String getBinStr() {
        return binStr;
    }

    public void setBinStr(String binStr) {
        this.binStr = binStr;
    }

    //序号
    private  double number;

    private  double pageNumber;

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public String getHmVersion() {
        return hwVersion;
    }

    public void setHmVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public double getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(double pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<String> getBinBytesStr() {
        return binBytesStr;
    }

    public void setBinBytesStr(List<String> binBytesStr) {
        this.binBytesStr = binBytesStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getM_userId() {
        return m_userId;
    }

    public void setM_userId(int m_userId) {
        this.m_userId = m_userId;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public String getBinSize() {
        return binSize;
    }

    public void setBinSize(String binSize) {
        this.binSize = binSize;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBinPath() {
        return binPath;
    }

    public void setBinPath(String binPath) {
        this.binPath = binPath;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public int getPacketTotal() {
        return packetTotal;
    }

    public void setPacketTotal(int packetTotal) {
        this.packetTotal = packetTotal;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
