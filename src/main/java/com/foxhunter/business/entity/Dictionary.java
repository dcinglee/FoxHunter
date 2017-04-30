package com.foxhunter.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxhunter.common.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 字典类，用于记录可增加的枚举类型。
 *
 * @author Ewing
 */
@Entity
public class Dictionary {
    // 字典ID
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String dictId;
    // 引用值
    private Integer value;
    // 字面值
    private String name;
    // 分类描述符
    private String type;
    // 创建时间
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public Dictionary() {
    }

    public Dictionary(String dictId) {
        this.dictId = dictId;
    }

    public Dictionary(Integer value, String name, String type) {
        this.value = value;
        this.name = name;
        this.type = type;
        this.createDate = new Date();
    }

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
