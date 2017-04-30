package com.foxhunter.custom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxhunter.common.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 客户实体类。
 *
 * @author Ewing
 */
@Entity
public class Custom {
    // 客户ID
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String customId;
    // 名称
    private String name;
    // 微信标识 (即小程序产生的openId)
    private String openId;
    // 性别ID
    private Integer genderValue;
    // 出生日期
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date bornDate;
    // 状态ID
    private Integer stateValue;
    //上传图片总数
    private Integer allPhotoNum;
    // 审核通过的图片数
    private Integer checkedPhotoNum;
    // 提供的黑名单数
    private Integer blacklistNum;
    // 历史收益总计
    private Float historyIncome;
    // 当前未提取的收益
    private Float income;
    // 身份证号码
    private String idCardNo;
    // 电话号码
    private String phoneNo;
    // 工作证号码
    private String workCardNo;
    // 系统/管理员批注
    private String annotate;
    // 备注信息
    private String notes;
    // 创建时间
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public Custom() {
    }

    public Custom(String customId) {
        this.customId = customId;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getGenderValue() {
        return genderValue;
    }

    public void setGenderValue(Integer genderValue) {
        this.genderValue = genderValue;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }

    public Integer getStateValue() {
        return stateValue;
    }

    public void setStateValue(Integer stateValue) {
        this.stateValue = stateValue;
    }

    public Integer getAllPhotoNum() {
        return allPhotoNum;
    }

    public void setAllPhotoNum(Integer allPhotoNum) {
        this.allPhotoNum = allPhotoNum;
    }

    public Integer getCheckedPhotoNum() {
        return checkedPhotoNum;
    }

    public void setCheckedPhotoNum(Integer checkedPhotoNum) {
        this.checkedPhotoNum = checkedPhotoNum;
    }

    public Integer getBlacklistNum() {
        return blacklistNum;
    }

    public void setBlacklistNum(Integer blacklistNum) {
        this.blacklistNum = blacklistNum;
    }

    public Float getHistoryIncome() {
        return historyIncome;
    }

    public void setHistoryIncome(Float historyIncome) {
        this.historyIncome = historyIncome;
    }

    public Float getIncome() {
        return income;
    }

    public void setIncome(Float income) {
        this.income = income;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getWorkCardNo() {
        return workCardNo;
    }

    public void setWorkCardNo(String workCardNo) {
        this.workCardNo = workCardNo;
    }

    public String getAnnotate() {
        return annotate;
    }

    public void setAnnotate(String annotate) {
        this.annotate = annotate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
