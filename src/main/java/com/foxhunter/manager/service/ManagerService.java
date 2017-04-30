package com.foxhunter.manager.service;

import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.manager.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 管理员服务类。
 *
 * @author Ewing
 */
public interface ManagerService {

    Manager addOrUpdateManager(Manager manager);

    Page<Manager> listMnagers(String name, String phoneNo, Pageable pageable);

    void deleteManager(Manager manager);

    ResultMessage managerLogin(String name, String password);

    Manager findManagerById(String managerId);

    Manager findFirstByName(String name);

    Manager findFirstByNameAndManagerIdNot(String name, String managerId);
}
