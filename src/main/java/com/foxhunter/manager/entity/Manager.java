package com.foxhunter.manager.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxhunter.common.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 管理员实体类。
 *
 * @author Ewing
 */
@Entity
public class Manager {
    //管理员ID
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String managerId;
    //名称
    private String name;
    //密码
    private String password;
    //性别
    private Integer genderValue;
    //出生日期
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date bornDate;
    //管理员类型
    private Integer typeValue;
    //状态值
    private String stateValue;
    //审核过的图片数量
    private Integer checkedPhotoNum;
    //新增黑名单的数量
    private Integer addBlacklistNum;
    //身份证号码
    private String idCardNo;
    //电话号码
    private String phoneNo;
    //工作证编号
    private String workCardNo;
    //超级管理员批注
    private String annotate;
    //备注
    private String notes;
    // 创建时间
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    // 构造方法

    public Manager() {
    }

    public Manager(String managerId) {
        this.managerId = managerId;
    }

    // Getters和Setters

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Integer getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(Integer typeValue) {
        this.typeValue = typeValue;
    }

    public String getStateValue() {
        return stateValue;
    }

    public void setStateValue(String stateValue) {
        this.stateValue = stateValue;
    }

    public Integer getCheckedPhotoNum() {
        return checkedPhotoNum;
    }

    public void setCheckedPhotoNum(Integer checkedPhotoNum) {
        this.checkedPhotoNum = checkedPhotoNum;
    }

    public Integer getAddBlacklistNum() {
        return addBlacklistNum;
    }

    public void setAddBlacklistNum(Integer addBlacklistNum) {
        this.addBlacklistNum = addBlacklistNum;
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