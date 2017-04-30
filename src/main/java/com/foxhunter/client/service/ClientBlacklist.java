package com.foxhunter.client.service;

/**
 * 给客户端查看的黑名单信息。
 *
 * @author Ewing
 */
public class ClientBlacklist {
    //名称
    private String name;
    //名单类型
    private Integer levelValue;
    //状态
    private Integer stateValue;
    //组织类型
    private Integer orgTypeValue;
    //电话
    private String phoneNo;
    //QQ号码
    private String qqNo;
    //微信号码
    private String microNo;
    //详细地址
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(Integer levelValue) {
        this.levelValue = levelValue;
    }

    public Integer getStateValue() {
        return stateValue;
    }

    public void setStateValue(Integer stateValue) {
        this.stateValue = stateValue;
    }

    public Integer getOrgTypeValue() {
        return orgTypeValue;
    }

    public void setOrgTypeValue(Integer orgTypeValue) {
        this.orgTypeValue = orgTypeValue;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getQqNo() {
        return qqNo;
    }

    public void setQqNo(String qqNo) {
        this.qqNo = qqNo;
    }

    public String getMicroNo() {
        return microNo;
    }

    public void setMicroNo(String microNo) {
        this.microNo = microNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
