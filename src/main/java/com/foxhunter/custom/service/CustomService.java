package com.foxhunter.custom.service;

import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.custom.entity.Custom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 客户服务类
 *
 * @author lijing
 * @2016年12月15日下午3:40:42
 */
public interface CustomService {

    Custom addOrUpdateCustom(Custom custom);

    Custom findCustomByOpenId(String openId);

    Page<Custom> listCustoms(String name, String phoneNo, Pageable pageable);

    ResultMessage updateCustomState(Custom newCustom);

    List<Map> countCustomAchievement(Custom custom);
    
    ResultMessage addCustom(Custom custom);

//	ResultMessage androidLogin(Custom custom, String code);

	ResultMessage phoneLogin(String phoneNo, String code);
    
    
}
