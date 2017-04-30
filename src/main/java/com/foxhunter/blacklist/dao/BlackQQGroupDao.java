package com.foxhunter.blacklist.dao;

import com.foxhunter.blacklist.entity.BlackQQGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * QQ群数据访问接口。
 */
public interface BlackQQGroupDao extends JpaRepository<BlackQQGroup, String>, JpaSpecificationExecutor<BlackQQGroup> {

    BlackQQGroup findFirstByGroupNo(String name);

    BlackQQGroup findFirstByStateValueOrderByCreateDateAsc(Integer state);
}
