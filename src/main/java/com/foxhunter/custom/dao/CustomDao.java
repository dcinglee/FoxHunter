package com.foxhunter.custom.dao;

import com.foxhunter.custom.entity.Custom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 客户数据库访问类。
 *
 * @author Ewing
 */


public interface CustomDao extends JpaRepository<Custom, String>, JpaSpecificationExecutor<Custom> {
    //根据openId查找对象确认是否存在
    public Custom findFirstByOpenId(String openId);

    public Custom findFirstByNameAndCustomIdNot(String name, String customId);
    
    public Custom findFirstByPhoneNo(String phoneNo);
    
    

}
