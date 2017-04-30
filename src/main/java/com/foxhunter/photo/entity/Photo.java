package com.foxhunter.photo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxhunter.common.config.AppConfig;
import com.foxhunter.custom.entity.Custom;
import com.foxhunter.manager.entity.Manager;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 照片实体，关联只配多对一和一对一。
 *
 * @author lijing
 */
@Entity
public class Photo {
    //照片ID
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String photoId;

    //处理该图片的管理员
    @ManyToOne
    @JoinColumn(name = "auditManagerId")
    private Manager auditManager;
    @Transient
    // 该属性不保存到数据库，保存要用对应的实体对象！
    private String auditManagerId;

    //上传该图片的客户
    @ManyToOne
    @JoinColumn(name = "createCustomId")
    private Custom createCustom;
    @Transient
    // 该属性不保存到数据库，保存要用对应的实体对象！
    private String createCustomId;

    //上传该图片的管理员
    @ManyToOne
    @JoinColumn(name = "createManagerId")
    private Manager createManager;
    @Transient
    // 该属性不保存到数据库，保存要用对应的实体对象！
    private String createManagerId;

    //照片名称
    private String fileName;

    //照片地址
    private String path;

    //格式
    private String format;

    //长度
    private Integer height;

    //宽度
    private Integer width;

    //尺寸
    private Integer size;

    //经度
    private Float longitude;

    //纬度
    private Float latitude;

    //创建时间
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    //审核时间
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date auditDate;

    //审核状态     1待审核  、 2审核通过、3审核未通过
    private Integer checkState;

    //审核备注
    private String auditInfo;

    public Photo() {
    }

    public Photo(String photoId) {
        this.photoId = photoId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public Manager getAuditManager() {
        return auditManager;
    }

    public void setAuditManager(Manager auditManager) {
        this.auditManager = auditManager;
    }

    public String getAuditManagerId() {
        return auditManagerId;
    }

    public void setAuditManagerId(String auditManagerId) {
        this.auditManagerId = auditManagerId;
    }

    public Custom getCreateCustom() {
        return createCustom;
    }

    public void setCreateCustom(Custom createCustom) {
        this.createCustom = createCustom;
    }

    public String getCreateCustomId() {
        return createCustomId;
    }

    public void setCreateCustomId(String createCustomId) {
        this.createCustomId = createCustomId;
    }

    public Manager getCreateManager() {
        return createManager;
    }

    public void setCreateManager(Manager createManager) {
        this.createManager = createManager;
    }

    public String getCreateManagerId() {
        return createManagerId;
    }

    public void setCreateManagerId(String createManagerId) {
        this.createManagerId = createManagerId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public String getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(String auditInfo) {
        this.auditInfo = auditInfo;
    }
}
