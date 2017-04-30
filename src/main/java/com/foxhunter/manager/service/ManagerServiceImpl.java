package com.foxhunter.manager.service;

import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.manager.dao.ManagerDao;
import com.foxhunter.manager.entity.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;

/**
 * 管理员服务类。
 *
 * @author Ewing
 */
@Service
public class ManagerServiceImpl implements ManagerService {
    private Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

    private ManagerDao managerDao;

    @Autowired
    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    @Override
    @Transactional
    public Manager addOrUpdateManager(Manager manager) {
        return managerDao.save(manager);
    }

    @Override
    public Page<Manager> listMnagers(String name, String phoneNo, Pageable pageable) {
        // 函数式接口（使用Lambda表达式）
        Specification<Manager> specification = (root, query, builder) -> {
            // 创建一个且(and)条件（述语）
            Predicate predicate = builder.conjunction();
            if (StringUtils.hasText(name)) {
                // 添加条件
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + name + "%"));
            }
            if (StringUtils.hasText(phoneNo)) {
                predicate = builder.and(predicate, builder.like(root.get("phoneNo"), "%" + phoneNo + "%"));
            }
            return predicate;
        };
        return managerDao.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public void deleteManager(Manager manager) {
        managerDao.delete(manager);
    }

    @Override
    public ResultMessage managerLogin(String name, String password) {
        Manager manager = managerDao.findByNameAndPassword(name, password);
        if (manager != null) {
            return ResultMessage.newSuccess("登陆成功！").setData(manager);
        } else {
            return ResultMessage.newFailure("管理员名称或密码不正确！");
        }
    }

    @Override
    public Manager findManagerById(String managerId) {
        return managerDao.findOne(managerId);
    }

    @Override
    public Manager findFirstByName(String name) {
        return managerDao.findFirstByName(name);
    }

    @Override
    public Manager findFirstByNameAndManagerIdNot(String name, String managerId) {
        return managerDao.findFirstByNameAndManagerIdNot(name, managerId);
    }


}
