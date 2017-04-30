package com.foxhunter.client.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxhunter.common.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 客户端实体类。
 *
 * @author Ewing
 */
@Entity
public class Client {
    //客户端ID
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String clientId;
    //客户端名称
    private String name;
    //客户端密码
    private String password;
    // 状态ID
    private Integer stateValue;
    //电话号码
    private String phoneNo;
    //联系邮箱
    private String mail;
    //余额
    private Float balance;
    //查询总条次
    private Long totalNum;
    //备注
    private String notes;
    // 创建时间
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    // 构造方法

    public Client() {
    }

    public Client(String clientId) {
        this.clientId = clientId;
    }

    // Getters 和 Setters

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public Integer getStateValue() {
        return stateValue;
    }

    public void setStateValue(Integer stateValue) {
        this.stateValue = stateValue;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
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