package com.foxhunter.business.service;

import com.foxhunter.business.entity.Dictionary;
import com.foxhunter.business.entity.Location;

import java.util.List;

/**
 * 业务服务接口。
 *
 * @author Ewing
 */
public interface BusinessService {
    /**
     * 获取字典值。
     */
    List<Dictionary> findDictionaryByType(String type);

    /**
     *  获取地理位置。
     */
    List<Location> findLocationByParentId(String parentId);
}
