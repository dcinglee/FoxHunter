package com.foxhunter.pay.service;

import com.foxhunter.common.config.AppConfig;
import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.custom.dao.CustomDao;
import com.foxhunter.custom.entity.Custom;
import com.foxhunter.pay.dao.BillDao;
import com.foxhunter.pay.entity.Bill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 提现业务的实现类
 *
 * @author albert
 *         2016年12月19日
 */
@Service
public class BillServiceImpl implements BillService {
    private Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

    private BillDao billDao;

    private CustomDao customDao;

    @Autowired
    public void setCustomDao(CustomDao customDao) {
        this.customDao = customDao;
    }

    @Autowired
    public void setBillDao(BillDao billDao) {
        this.billDao = billDao;
    }

    @Override
    public Page<Bill> findByCustomOpenId(String openId, Pageable pageable) {
        if (null != openId && !"".equals(openId)) {
            Page<Bill> billPage = billDao.findByCustomOpenIdOrderByCreateTimeDesc(openId, pageable);
            return billPage;
        }
        return null;
    }

    @Override
    public Page<Bill> findBills(Date startTime, Date endTime, String name, String billNo, Pageable pageable) {
        Specification<Bill> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (startTime != null) {
                predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("createTime"), startTime));
            }
            if (endTime != null) {
                predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("createTime"), endTime));
            }
            if (StringUtils.hasText(name)) {
                predicate = builder.and(predicate, builder.like(root.get("custom").get("name"), "%" + name + "%"));
            }
            if (StringUtils.hasText(billNo)) {
                predicate = builder.and(predicate, builder.like(root.get("billNo"), "%" + billNo + "%"));
            }
            query.orderBy(builder.desc(root.get("createTime")));
            return predicate;
        };
        return billDao.findAll(specification, pageable);
    }

    /**
     * 客户提现。
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public ResultMessage withdrawCash(Custom customIn) {
        Custom custom = customDao.findFirstByOpenId(customIn.getOpenId());
        if (custom == null || custom.getCustomId() == null) {
            return ResultMessage.newFailure("没有查询到客户信息！");
        }
        if (custom.getIncome() < AppConfig.MIN_WITHDRAW_CASH) {
            return ResultMessage.newFailure("金额不足" + AppConfig.MIN_WITHDRAW_CASH + "元，不能提现。");
        }
        // TODO 此处支付需要实现
        // 保存账单
        Bill bill = new Bill();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        bill.setCreateTime(new Date());
        bill.setBillNo(format.format(bill.getCreateTime()));
        bill.setCurrencyTypeValue(1);
        bill.setStateValue(2);
        bill.setSum(custom.getIncome());
        bill.setCustom(custom);
        billDao.save(bill);
        // 减去金额
        custom.setHistoryIncome(custom.getHistoryIncome() + custom.getIncome());
        custom.setIncome(0f);
        customDao.save(custom);
        return ResultMessage.newSuccess("提现成功！");
    }


}
