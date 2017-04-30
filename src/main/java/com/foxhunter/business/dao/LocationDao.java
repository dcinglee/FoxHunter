package com.foxhunter.business.dao;

import com.foxhunter.business.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 字典数据库访问类。
 *
 * @author Ewing
 */
public interface LocationDao extends JpaRepository<Location, String> {
    /**
     *  获取地理位置。
     */
    List<Location> findByParentIdOrderBySort(String parentId);
}
