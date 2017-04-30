package com.foxhunter.business.service;

import com.foxhunter.business.dao.DictionaryDao;
import com.foxhunter.business.dao.LocationDao;
import com.foxhunter.business.entity.Dictionary;
import com.foxhunter.business.entity.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务服务实现类。
 *
 * @author Ewing
 */
@Service
public class BusinessServiceImpl implements BusinessService {
    private Logger logger = LoggerFactory.getLogger(BusinessServiceImpl.class);

    private DictionaryDao dictionaryDao;
    private LocationDao locationDao;

    @Autowired
    public void setDictionaryDao(DictionaryDao dictionaryDao) {
        this.dictionaryDao = dictionaryDao;
    }

    @Autowired
    public void setLocationDao(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    /**
     * 根据类型获取字典值。
     */
    @Override
    public List<Dictionary> findDictionaryByType(String type) {
        return dictionaryDao.findByTypeOrderByValue(type);
    }

    /**
     * 获取地理位置信息。
     */
    @Override
    public List<Location> findLocationByParentId(String parentId) {
        return locationDao.findByParentIdOrderBySort(parentId);
    }
}
