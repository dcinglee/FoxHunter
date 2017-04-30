package com.foxhunter.blacklist.service;

import com.foxhunter.blacklist.dao.BlackQQGroupDao;
import com.foxhunter.blacklist.entity.BlackQQGroup;
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
import java.util.Date;

/**
 * QQ群服务实现类。
 */
@Service
public class BlackQQGroupServiceImpl implements BlackQQGroupService {
    private Logger logger = LoggerFactory.getLogger(BlackQQGroupServiceImpl.class);

    private BlackQQGroupDao blackQQGroupDao;

    @Autowired
    public void setBlackQQGroupDao(BlackQQGroupDao blackQQGroupDao) {
        this.blackQQGroupDao = blackQQGroupDao;
    }

    /**
     * 新增时的标准化校验。
     */
    private BlackQQGroup addStandardize(BlackQQGroup blackQQGroup) {
        if (blackQQGroup == null)
            throw new RuntimeException("QQ群不能为空！");
        if (!StringUtils.hasText(blackQQGroup.getGroupNo()))
            throw new RuntimeException("QQ群号不能为空！");
        if (!StringUtils.hasText(blackQQGroup.getMyQQ()))
            throw new RuntimeException("我的QQ号不能为空！");
        if (!StringUtils.hasText(blackQQGroup.getMyPassword()))
            throw new RuntimeException("我的密码不能为空！");
        if (blackQQGroup.getStateValue() == null)
            blackQQGroup.setStateValue(0);
        return blackQQGroup;
    }

    /**
     * 修改时的标准化校验。
     */
    private BlackQQGroup updateStandardize(BlackQQGroup blackQQGroup) {
        if (blackQQGroup == null || !StringUtils.hasText(blackQQGroup.getGroupId()))
            throw new RuntimeException("QQ群和ID不能为空！");
        if (!StringUtils.hasText(blackQQGroup.getMyQQ()))
            throw new RuntimeException("我的QQ号不能为空！");
        if (!StringUtils.hasText(blackQQGroup.getMyPassword()))
            throw new RuntimeException("我的密码不能为空！");
        if (blackQQGroup.getStateValue() == null)
            blackQQGroup.setStateValue(0);
        return blackQQGroup;
    }

    @Override
    @Transactional
    public BlackQQGroup addQQGroup(BlackQQGroup blackQQGroup) {
        addStandardize(blackQQGroup); // 标准化校验
        if (blackQQGroupDao.findFirstByGroupNo(blackQQGroup.getGroupNo()) != null) {
            throw new RuntimeException("该条QQ群号已存在！");
        }
        blackQQGroup.setCreateDate(new Date());
        return blackQQGroupDao.save(blackQQGroup);
    }

    @Override
    public Page<BlackQQGroup> listBlackQQGroups(String groupNo, String myQQ, Pageable pageable) {
        Specification<BlackQQGroup> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction(); // 创建且(and)条件，初始为1=1。
            if (StringUtils.hasText(groupNo)) { // 名称不空白，添加到且条件。
                predicate = builder.and(predicate, builder.like(root.get("groupNo"), "%" + groupNo + "%"));
            }
            if (StringUtils.hasText(myQQ)) { // 电话号码不空白，添加到且条件。
                predicate = builder.and(predicate, builder.like(root.get("myQQ"), "%" + myQQ + "%"));
            }
            return predicate;
        };
        return blackQQGroupDao.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public BlackQQGroup updateQQGroup(BlackQQGroup blackQQGroup) {
        updateStandardize(blackQQGroup); // 标准化校验
        BlackQQGroup oldQQGroup = blackQQGroupDao.findOne(blackQQGroup.getGroupId());
        if (oldQQGroup == null) {
            throw new RuntimeException("QQ群不存在或已删除！");
        }
        oldQQGroup.setMyQQ(blackQQGroup.getMyQQ());
        oldQQGroup.setMyPassword(blackQQGroup.getMyPassword());
        oldQQGroup.setStateValue(blackQQGroup.getStateValue());
        return blackQQGroupDao.save(oldQQGroup);
    }

}
