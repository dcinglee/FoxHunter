package com.foxhunter.blacklist.web;

import com.foxhunter.blacklist.entity.BlackQQGroup;
import com.foxhunter.blacklist.service.BlackQQGroupService;
import com.foxhunter.common.interceptor.AuthLogin;
import com.foxhunter.common.vo.EasyUIGridData;
import com.foxhunter.common.vo.ResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * QQ群控制器。
 */
@Controller
@RequestMapping("/blackQQGroup")
@AuthLogin(needManager = true)
public class BlackQQGroupController {
    private Logger logger = LoggerFactory.getLogger(BlackQQGroupController.class);
    private BlackQQGroupService blackQQGroupService;

    @Autowired
    public void setBlackQQGroupService(BlackQQGroupService blackQQGroupService) {
        this.blackQQGroupService = blackQQGroupService;
    }

    /**
     * 新增QQ群。
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addQQGroup(BlackQQGroup blackQQGroup) {
        try {
            BlackQQGroup qqGroupNew = blackQQGroupService.addQQGroup(blackQQGroup);
            return ResultMessage.newSuccess("新增QQ群成功！").setData(qqGroupNew);
        } catch (Exception e) {
            logger.error("新增QQ群异常：" + e.getMessage());
            return ResultMessage.newFailure("新增QQ群失败：" + e.getMessage());
        }
    }

    /**
     * 编辑QQ群。
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage updateQQGroup(BlackQQGroup blackQQGroup) {
        try {
            BlackQQGroup qqGroupNew = blackQQGroupService.updateQQGroup(blackQQGroup);
            return ResultMessage.newSuccess("编辑QQ群成功！").setData(qqGroupNew);
        } catch (Exception e) {
            logger.error("编辑QQ群异常：" + e.getMessage());
            return ResultMessage.newFailure("编辑QQ群失败：" + e.getMessage());
        }
    }

    /**
     * 查询QQ群。
     */
    @ResponseBody
    @RequestMapping("/list")
    public EasyUIGridData listBlacklists(int page, int rows, String groupNo, String myQQ) {
        try {
            Pageable pageable = new PageRequest(page - 1, rows);
            Page dataPage = blackQQGroupService.listBlackQQGroups(groupNo, myQQ, pageable);
            return new EasyUIGridData(dataPage);
        } catch (Exception e) {
            logger.error("查询QQ群异常：" + e.getMessage());
            return new EasyUIGridData("groupNo", "查询数据异常！");
        }
    }

}
