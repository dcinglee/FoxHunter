package com.foxhunter.manager.dao;

import com.foxhunter.manager.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 管理员数据库访问接口。
 * 该接口使用了Spring Data JPA提供的方法。
 *
 * @author Ewing
 */
public interface ManagerDao extends JpaRepository<Manager, String>, JpaSpecificationExecutor<Manager> {

    Manager findByNameAndPassword(String name, String password);

    Manager findFirstByName(String name);

    Manager findFirstByNameAndManagerIdNot(String name, String managerId);
}
