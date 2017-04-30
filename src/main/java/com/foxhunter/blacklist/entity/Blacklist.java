package com.foxhunter.blacklist.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxhunter.business.entity.Location;
import com.foxhunter.common.config.AppConfig;
import com.foxhunter.manager.entity.Manager;
import com.foxhunter.photo.entity.Photo;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 黑名单实体类。
 *
 * @author Ewing
 */
@Entity
public class Blacklist {
    //黑名单ID
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    private String blacklistId;

    //名称
    private String name;

    //昵称
    private String nickName;

    //英文名称
    private String enName;

    //出生日期
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date bornDate;

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

    //身份证号
    private String idCardNo;

    //银行卡号
    private String bankCards;

    //经度位置
    private Float longitude;

    //纬度位置
    private Float latitude;

    // 所在省份
    @ManyToOne
    @JoinColumn(name = "provinceId")
    private Location province;
    //省份ID
    @Transient
    // 该属性不保存到数据库，只保存province。
    private String provinceId;

    // 所在城市
    @ManyToOne
    @JoinColumn(name = "cityId")
    private Location city;
    //城市ID
    @Transient
    // 该属性不保存到数据库，只保存city。
    private String cityId;

    // 所在区县
    @ManyToOne
    @JoinColumn(name = "countyId")
    private Location county;
    //区县ID
    @Transient
    // 该属性不保存到数据库，只保存county。
    private String countyId;

    //详细地址
    private String address;

    //邮箱
    private String email;

    //个人主页
    private String homeUrl;

    //学历
    private Integer graduteValue;

    //来源类型
    private Integer fromTypeValue;

    //来源图片
    @ManyToOne
    @JoinColumn(name = "fromPhotoId")
    private Photo fromPhoto;
    @Transient
    // 该属性不保存到数据库，保存要用fromPhoto属性！
    private String fromPhotoId;

    //来源信息
    private String fromInfo;

    //录入的管理员
    @ManyToOne
    @JoinColumn(name = "addManagerId")
    private Manager addManager;
    @Transient
    // 该属性不保存到数据库，保存要用addManager属性！
    private String addManagerId;

    //创建时间
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public Blacklist() {
    }

    public Blacklist(String blacklistId) {
        this.blacklistId = blacklistId;
    }

    public String getBlacklistId() {
        return blacklistId;
    }

    public void setBlacklistId(String blacklistId) {
        this.blacklistId = blacklistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
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

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getBankCards() {
        return bankCards;
    }

    public void setBankCards(String bankCards) {
        this.bankCards = bankCards;
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

    public Location getProvince() {
        return province;
    }

    public void setProvince(Location province) {
        this.province = province;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public Location getCity() {
        return city;
    }

    public void setCity(Location city) {
        this.city = city;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public Location getCounty() {
        return county;
    }

    public void setCounty(Location county) {
        this.county = county;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public Integer getGraduteValue() {
        return graduteValue;
    }

    public void setGraduteValue(Integer graduteValue) {
        this.graduteValue = graduteValue;
    }

    public Integer getFromTypeValue() {
        return fromTypeValue;
    }

    public void setFromTypeValue(Integer fromTypeValue) {
        this.fromTypeValue = fromTypeValue;
    }

    public Photo getFromPhoto() {
        return fromPhoto;
    }

    public void setFromPhoto(Photo fromPhoto) {
        this.fromPhoto = fromPhoto;
    }

    public String getFromPhotoId() {
        return fromPhotoId;
    }

    public void setFromPhotoId(String fromPhotoId) {
        this.fromPhotoId = fromPhotoId;
    }

    public String getFromInfo() {
        return fromInfo;
    }

    public void setFromInfo(String fromInfo) {
        this.fromInfo = fromInfo;
    }

    public Manager getAddManager() {
        return addManager;
    }

    public void setAddManager(Manager addManager) {
        this.addManager = addManager;
    }

    public String getAddManagerId() {
        return addManagerId;
    }

    public void setAddManagerId(String addManagerId) {
        this.addManagerId = addManagerId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}