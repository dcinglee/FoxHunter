/**
 *
 */
package com.foxhunter.custom.web;

import com.foxhunter.common.interceptor.AuthLogin;
import com.foxhunter.common.micro.MicroAccessResult;
import com.foxhunter.common.micro.MicroAccessor;
import com.foxhunter.common.util.EHCacheUtil;
import com.foxhunter.common.vo.EasyUIGridData;
import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.custom.entity.Custom;
import com.foxhunter.custom.service.CustomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 客户控制层。
 *
 * @author lijing
 * @2016年12月15日下午3:38:49
 */
@Controller
@RequestMapping("/custom")
public class CustomController {
    private Logger logger = LoggerFactory.getLogger(CustomController.class);

    private CustomService customService;
    

    @Autowired
    public void setCustomService(CustomService customService) {
        this.customService = customService;
    }
    
    /**
     * 微信客户端认证（小程序专用）。
     */
    @ResponseBody
    @RequestMapping("/auth")
    public ResultMessage authCustom(String code,String source) {
        try {
            // 微信授权认证。
            MicroAccessResult authResult = MicroAccessor.authForMicroApp(code,source);
            String openId = authResult.getOpenid();
            String sessionKey = authResult.getSession_key();
            if (!StringUtils.hasText(openId) || !StringUtils.hasText(sessionKey)) {
                return ResultMessage.newFailure("微信授权登陆失败！").setData(authResult);
            }
            // 使用Custom保持缓存数据类型一致。
            Custom custom = new Custom();
            custom.setOpenId(openId);
            EHCacheUtil.put(openId, custom);
            return ResultMessage.newSuccess().setData(authResult);
        } catch (Exception e) {
            logger.error("微信登陆失败：" + e.getMessage());
            return ResultMessage.newFailure("微信登陆失败！");
        }
    }
    
    /**
     * 客户认证，首次认证增加客户。
     */
    @ResponseBody
    @RequestMapping("/login")
    public ResultMessage addCustom(Custom custom) {
        try {
        	return customService.addCustom(custom);
        } catch (Exception e) {
            logger.error("新增客户失败：" + e.getMessage());
            return ResultMessage.newFailure("新增客户失败！");
        }
    }
    
    /**
     * 客户认证，首次认证增加客户。
     */
//    @ResponseBody
//    @RequestMapping("/androidLogin")
//    public ResultMessage androidLogin(Custom custom, String code) {
//        try {
//        	return customService.androidLogin(custom, code);
//        } catch (Exception e) {
//            logger.error("新增客户失败：" + e.getMessage());
//            return ResultMessage.newFailure("新增客户失败！");
//        }
//    }
    
    /**
     * 客户认证，首次认证增加客户。
     */
    @ResponseBody
    @RequestMapping("/phoneLogin")
    public ResultMessage phoneLogin(String phoneNo, String code) {
        try {
        	return customService.phoneLogin(phoneNo, code);
        } catch (Exception e) {
            logger.error("新增客户失败：" + e.getMessage());
            return ResultMessage.newFailure("新增客户失败！");
        }
    }

    /**
     * 查询客户。
     */
    @ResponseBody
    @RequestMapping("/list")
    @AuthLogin(needManager = true)
    public EasyUIGridData listCustoms(int page, int rows, String name, String phoneNo) {
        try {
            Pageable pageable = new PageRequest(page - 1, rows);
            Page<Custom> dataPage = customService.listCustoms(name, phoneNo, pageable);
            return new EasyUIGridData(dataPage);
        } catch (Exception e) {
            logger.error("查询客户异常：" + e.getMessage());
            return new EasyUIGridData("name", "查询客户异常！");
        }
    }

    /**
     * 编辑客户。
     */
    @ResponseBody
    @RequestMapping("/update")
    @AuthLogin(needManager = true)
    public ResultMessage updateCustom(Custom newCustom) {
        try {
            return customService.updateCustomState(newCustom);
        } catch (Exception e) {
            logger.error("编辑客户异常：" + e.getMessage());
            return ResultMessage.newFailure("编辑客户失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/achievement")
    @AuthLogin(needCustom = true)
    public ResultMessage achievement(Custom custom) {
        try {
            List<Map> achievement = customService.countCustomAchievement(custom);
            return ResultMessage.newSuccess().setData(achievement);
        } catch (Exception e) {
            logger.error("统计客户绩效异常：" + e.getMessage());
            return ResultMessage.newFailure("统计客户绩效失败！");
        }
    }

}
