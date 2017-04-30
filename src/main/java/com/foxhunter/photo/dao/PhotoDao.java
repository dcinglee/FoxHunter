package com.foxhunter.photo.dao;

import com.foxhunter.photo.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 照片dao层
 * 该接口使用了Spring Data JPA提供的方法。
 *
 * @author lijing
 * @2016年12月14日上午9:56:27
 */
public interface PhotoDao extends JpaRepository<Photo, String>, JpaSpecificationExecutor<Photo> {

    Page<Photo> findByCreateCustomCustomIdAndCheckStateOrderByCreateDateDesc(String customId, Integer checkState, Pageable pageable);

    Photo findFirstByCheckStateAndCreateDateGreaterThanOrderByCreateDateAsc(Integer checkState, Date createDate);

    @Query("select p.checkState as checkState,count(p.photoId) as total from Photo p" +
            " where p.createCustom.customId = ?1 group by p.checkState order by p.checkState")
    List<Map> countByCreateCustomGroupByCheckState(String customId);
}
