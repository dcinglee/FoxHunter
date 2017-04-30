package com.foxhunter.business.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 地理位置信息表。
 *
 * @author Ewing
 */
@Entity
public class Location {
    @Id
    private String locationId;
    private String parentId;
    private String name;
    private String shortName;
    private String longitude;
    private String latitude;
    private String level;
    private String sort;

    public Location() {
    }

    public Location(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
