package com.foxhunter.pay.dao;

import com.foxhunter.pay.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 账单数据库访问类。
 *
 * @author Ewing
 */
public interface BillDao extends JpaRepository<Bill, String>, JpaSpecificationExecutor<Bill> {

    Page<Bill> findByCustomOpenIdOrderByCreateTimeDesc(String openId, Pageable pageable);

}

