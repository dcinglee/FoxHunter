package com.foxhunter.manager.web;

import com.foxhunter.common.config.AppConfig;
import com.foxhunter.common.interceptor.AuthLogin;
import com.foxhunter.common.util.EncryptionUtil;
import com.foxhunter.common.vo.EasyUIGridData;
import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.manager.entity.Manager;
import com.foxhunter.manager.service.ManagerService;
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

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员模块请求控制器。
 *
 * @author Ewing
 */
@Controller
@RequestMapping("/manager")
@AuthLogin(needManager = true)
public class ManagerController {
    private Logger logger = LoggerFactory.getLogger(ManagerController.class);

    private ManagerService managerService;

    @Autowired
    public void setManagerService(ManagerService managerService) {
        this.managerService = managerService;
    }

    /**
     * 新增管理员。
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addManager(Manager manager, HttpSession session) {
        try {
            Manager loginer = (Manager) session.getAttribute(AppConfig.MANAGER_SESSION_KEY);
            if (loginer == null || loginer.getTypeValue() != 1) {
                return ResultMessage.newFailure("需要超级管理员登陆！");
            }
            String name = manager.getName();
            if (!StringUtils.hasText(name)) {
                return ResultMessage.newFailure("管理员名称不能为空！");
            }
            if (managerService.findFirstByName(name) != null) {
                return ResultMessage.newFailure("该管理员名称已存在！");
            }
            manager.setAddBlacklistNum(0);
            manager.setCheckedPhotoNum(0);
            manager.setTypeValue(2);
            manager.setCreateDate(new Date());
            Manager newManager = managerService.addOrUpdateManager(manager);
            if (newManager != null && newManager.getManagerId() != null) {
                return ResultMessage.newSuccess("新增管理员成功！");
            } else {
                return ResultMessage.newFailure("新增管理员失败！");
            }
        } catch (Exception e) {
            logger.error("新增管理员失败：" + e.getMessage());
            return ResultMessage.newFailure("新增管理员异常！");
        }
    }

    /**
     * 更新管理员。
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage updateManager(Manager newManager, HttpSession session) {
        try {
            Manager loginer = (Manager) session.getAttribute(AppConfig.MANAGER_SESSION_KEY);
            if (loginer == null || (loginer.getTypeValue() != 1 && !loginer.getManagerId().equals(newManager.getManagerId()))) {
                return ResultMessage.newFailure("请用超级管理员登陆！");
            }
            String name = newManager.getName();
            if (!StringUtils.hasText(name)) {
                return ResultMessage.newFailure("管理员名称不能为空！");
            }
            Manager manager = managerService.findManagerById(newManager.getManagerId());
            if (manager == null) {
                logger.error("修改管理员时根据ID未找到记录：" + newManager.getManagerId());
                return ResultMessage.newFailure("该管理员不存在或已删除！");
            }
            if (managerService.findFirstByNameAndManagerIdNot(name, manager.getManagerId()) != null) {
                return ResultMessage.newFailure("该管理员名称已存在！");
            }
            // 对需要修改的字段做控制，不能使用BeanUtil工具复制全部字段。
            manager.setName(newManager.getName());
            manager.setPassword(newManager.getPassword());
            manager.setGenderValue(newManager.getGenderValue());
            manager.setBornDate(newManager.getBornDate());
            manager.setPhoneNo(newManager.getPhoneNo());
            manager.setIdCardNo(newManager.getIdCardNo());
            manager.setWorkCardNo(newManager.getWorkCardNo());
            manager.setNotes(newManager.getNotes());
            managerService.addOrUpdateManager(manager);
            return ResultMessage.newSuccess("修改管理员成功！");
        } catch (Exception e) {
            logger.error("修改管理员异常：" + e.getMessage());
            return ResultMessage.newFailure("修改管理员异常！");
        }
    }

    /**
     * 删除管理员。
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResultMessage deleteManager(Manager manager, HttpSession session) {
        try {
            Manager loginer = (Manager) session.getAttribute(AppConfig.MANAGER_SESSION_KEY);
            if (loginer == null || loginer.getTypeValue() != 1) {
                return ResultMessage.newFailure("需要超级管理员登陆！");
            }
            managerService.deleteManager(manager);
            return ResultMessage.newSuccess("删除管理员成功！");
        } catch (Exception e) {
            logger.error("删除管理员异常：" + e.getMessage());
            return ResultMessage.newFailure("删除管理员异常！");
        }
    }

    /**
     * 查询管理员。
     */
    @ResponseBody
    @RequestMapping("/list")
    public EasyUIGridData listManagers(int page, int rows, String name, String phoneNo) {
        try {
            Pageable pageable = new PageRequest(page - 1, rows);
            Page dataPage = managerService.listMnagers(name, phoneNo, pageable);
            return new EasyUIGridData(dataPage);
        } catch (Exception e) {
            return new EasyUIGridData("name", "查询数据异常！");
        }
    }

    /**
     * 管理员登陆。
     */
    @ResponseBody
    @RequestMapping("/rsaKey")
    @AuthLogin(needManager = false)
    public ResultMessage getRsaKey(String name) {
        try {
            Map<String, String> rsaKey = new HashMap<>(2);
            String modulus = EncryptionUtil.getModulus().toString(16);
            String exponent = EncryptionUtil.getPublicExponent().toString(16);
            rsaKey.put("modulus", modulus);
            rsaKey.put("exponent", exponent);
            return ResultMessage.newSuccess().setData(rsaKey);
        } catch (Exception e) {
            logger.error("获取公钥异常：" + e.getMessage());
            return ResultMessage.newFailure("获取公钥异常！");
        }
    }

    /**
     * 管理员登陆。
     */
    @ResponseBody
    @RequestMapping("/login")
    @AuthLogin(needManager = false)
    public ResultMessage managerLogin(String name, String password, HttpSession session) {
        try {
            password = EncryptionUtil.decryptString(password);
            ResultMessage message = managerService.managerLogin(name, password);
            if (message.isSuccess()) {
                Manager manager = message.getData();
                session.setAttribute(AppConfig.MANAGER_SESSION_KEY, manager);
            }
            return message;
        } catch (Exception e) {
            logger.error("管理员登陆异常：" + e.getMessage());
            return ResultMessage.newFailure("登陆异常！");
        }
    }


    /**
     * 获取当前登陆的管理员。
     */
    @ResponseBody
    @RequestMapping("/loginer")
    public ResultMessage managerLoginer(HttpSession session) {
        Manager manager = (Manager) session.getAttribute(AppConfig.MANAGER_SESSION_KEY);
        if (manager == null) {
            return ResultMessage.newFailure("当前未登陆或会话超时！");
        } else {
            return ResultMessage.newSuccess().setData(manager);
        }
    }

    /**
     * 管理员退出。
     */
    @ResponseBody
    @RequestMapping("/exit")
    @AuthLogin(needManager = false)
    public ResultMessage managerExit(HttpSession session) {
        session.removeAttribute(AppConfig.MANAGER_SESSION_KEY);
        return ResultMessage.newSuccess();
    }

}
