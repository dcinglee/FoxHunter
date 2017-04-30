package com.foxhunter.custom.service;

import com.foxhunter.common.micro.MicroAccessResult;
import com.foxhunter.common.micro.MicroAccessor;
import com.foxhunter.common.util.EHCacheUtil;
import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.custom.dao.CustomDao;
import com.foxhunter.custom.entity.Custom;
import com.foxhunter.photo.dao.PhotoDao;
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
import java.util.List;
import java.util.Map;

/**
 * 客户服务类
 *
 * @author lijing
 * @2016年12月15日下午3:41:33
 */
@Service
public class CustomServiceImpl implements CustomService {
    private Logger logger = LoggerFactory.getLogger(CustomServiceImpl.class);

    private CustomDao customDao;

    private PhotoDao photoDao;
    
    @Autowired
    public void setPhotoDao(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    @Autowired
    public void setCustomDao(CustomDao customDao) {
        this.customDao = customDao;
    }

    @Override
    @Transactional
    public Custom addOrUpdateCustom(Custom custom) {
        return customDao.save(custom);
    }

    @Override
    public Custom findCustomByOpenId(String openId) {
        return customDao.findFirstByOpenId(openId);
    }

    @Override
    public Page<Custom> listCustoms(String name, String phoneNo, Pageable pageable) {
        Specification<Custom> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.hasText(name)) {
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + name + "%"));
            }
            if (StringUtils.hasText(phoneNo)) {
                predicate = builder.and(predicate, builder.like(root.get("phoneNo"), "%" + phoneNo + "%"));
            }
            return predicate;
        };
        return customDao.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public ResultMessage updateCustomState(Custom newCustom) {
        String name = newCustom.getName();
        Custom custom = customDao.findOne(newCustom.getCustomId());
        if (custom == null) {
            logger.error("客户编辑失败，根据客户ID未找到记录：" + newCustom.getCustomId());
            return ResultMessage.newFailure("该客户不存在或已删除！");
        }
        if (customDao.findFirstByNameAndCustomIdNot(name, custom.getCustomId()) != null) {
            return ResultMessage.newFailure("该客户名称已存在！");
        }
        //对需要操作的字段做控制
        custom.setStateValue(newCustom.getStateValue());
        custom.setAnnotate(newCustom.getAnnotate());
        customDao.save(custom);
        return ResultMessage.newSuccess("编辑客户成功！");
    }

    @Override
    public List<Map> countCustomAchievement(Custom custom) {
        if (!StringUtils.hasText(custom.getOpenId())) {
            throw new RuntimeException("客户openId不能为空！");
        }
        custom = customDao.findFirstByOpenId(custom.getOpenId());
        return photoDao.countByCreateCustomGroupByCheckState(custom.getCustomId());
    }

	@Override
	public ResultMessage addCustom(Custom custom) {
        String openId = custom.getOpenId();
        if (openId == null || EHCacheUtil.get(openId) == null) {
            return ResultMessage.newFailure("请先授权微信登陆！");
        }
        // 更新并返回信息。
        Custom existsCustom = customDao.findFirstByOpenId(openId);
        if (existsCustom != null) {
            existsCustom.setName(custom.getName());
            existsCustom.setGenderValue(custom.getGenderValue());
            customDao.save(existsCustom);
            EHCacheUtil.put(openId, existsCustom);
            return ResultMessage.newSuccess("客户认证成功！");
        }
        // 客户不存在，创建新客户。
        custom.setStateValue(1);
        custom.setCheckedPhotoNum(0);
        custom.setBlacklistNum(0);
        custom.setAllPhotoNum(0);
        custom.setHistoryIncome(0f);
        custom.setIncome(0f);
        custom.setCreateDate(new Date());
        Custom newCustom = customDao.save(custom);
        if (newCustom != null) {
            EHCacheUtil.put(openId, newCustom);
            return ResultMessage.newSuccess("客户认证成功");
        } else {
            return ResultMessage.newFailure("客户认证失败！");
        }
	}

	@Override
	public ResultMessage phoneLogin(String phoneNo, String code) {
		String _code = EHCacheUtil.get(phoneNo+"code");
		if("".equals(_code) && !_code.equals(code)){
			return ResultMessage.newFailure("短信验证码错误！");
		}
		Custom custom = customDao.findFirstByPhoneNo(phoneNo);
		String customId = custom.getCustomId();
		if(customId != null || EHCacheUtil.get(customId) != null){
			custom.setName(custom.getName());
			custom.setGenderValue(custom.getGenderValue());
			customDao.save(custom);
			EHCacheUtil.put(customId, custom);
			EHCacheUtil.remove(phoneNo+"code");
			return ResultMessage.newSuccess("短信验证成功！");
		}
		 // 客户不存在，创建新客户。
        custom.setStateValue(1);
        custom.setCheckedPhotoNum(0);
        custom.setBlacklistNum(0);
        custom.setAllPhotoNum(0);
        custom.setHistoryIncome(0f);
        custom.setIncome(0f);
        custom.setCreateDate(new Date());
        Custom newCustom = customDao.save(custom);
        if (newCustom != null) {
            EHCacheUtil.put(customId, newCustom);
			EHCacheUtil.remove(phoneNo+"code");
			return ResultMessage.newSuccess("短信验证成功！");
        } else {
            return ResultMessage.newFailure("短信验证失败！");
        }
	}

//	@Override
//	public ResultMessage androidLogin(Custom custom, String code) {
//
//		 MicroAccessResult result = MicroAccessor.authToMicro(code);
//		 String openId = result.getOpenid();
//		 MicroAccessResult newresult = MicroAccessor.getMicroUserInfo(result.getAccess_token(), openId);
//		 Custom existsCustom = customDao.findFirstByOpenId(openId);
//		if(existsCustom != null){
//			existsCustom.setName(custom.getName());
//			existsCustom.setGenderValue(custom.getGenderValue());
//			customDao.save(existsCustom);
//	        EHCacheUtil.put(openId, existsCustom);
//	        return ResultMessage.newSuccess("客户认证成功！");		
//		}
//		 // 客户不存在，创建新客户。
//        custom.setStateValue(1);
//        custom.setCheckedPhotoNum(0);
//        custom.setBlacklistNum(0);
//        custom.setAllPhotoNum(0);
//        custom.setHistoryIncome(0f);
//        custom.setIncome(0f);
//        custom.setCreateDate(new Date());
//        Custom newCustom = customDao.save(custom);
//        if (newCustom != null) {
//            EHCacheUtil.put(openId, newCustom);
//            return ResultMessage.newSuccess("客户认证成功");
//        } else {
//            return ResultMessage.newFailure("客户认证失败！");
//        }
//	}
}
