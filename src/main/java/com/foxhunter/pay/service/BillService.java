package com.foxhunter.pay.service;

import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.custom.entity.Custom;
import com.foxhunter.pay.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * 账单服务接口。
 *
 * @author albert
 *         2016年12月15日
 */
public interface BillService {

    Page<Bill> findByCustomOpenId(String openId, Pageable pageable);

    Page<Bill> findBills(Date startTime, Date endTime, String name, String billNo, Pageable pageable);

    ResultMessage withdrawCash(Custom custom);
}
