package com.foxhunter.pay.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxhunter.common.config.AppConfig;
import com.foxhunter.custom.entity.Custom;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 账单实体
 *
 * @author Ewing
 */
@Entity
public class Bill {
    // 账单ID
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String billId;
    // 编号
    private String billNo;
    // 状态
    private Integer stateValue;
    // 客户
    @ManyToOne
    @JoinColumn(name = "customId")
    private Custom custom;
    @Transient
    // 该属性不保存到数据库，保存要用对应的实体对象！
    private String customId;
    // 金额
    private Float sum;
    // 货币类型
    private Integer currencyTypeValue;
    // 创建时间
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createTime;

    public Bill() {
    }

    public Bill(String billId) {
        this.billId = billId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getStateValue() {
        return stateValue;
    }

    public void setStateValue(Integer stateValue) {
        this.stateValue = stateValue;
    }

    public Custom getCustom() {
        return custom;
    }

    public void setCustom(Custom custom) {
        this.custom = custom;
    }

    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public Integer getCurrencyTypeValue() {
        return currencyTypeValue;
    }

    public void setCurrencyTypeValue(Integer currencyTypeValue) {
        this.currencyTypeValue = currencyTypeValue;
    }


    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

}