package com.foxhunter.blacklist.web;

import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.blacklist.service.BlacklistService;
import com.foxhunter.common.config.AppConfig;
import com.foxhunter.common.interceptor.AuthLogin;
import com.foxhunter.common.vo.EasyUIGridData;
import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.manager.entity.Manager;
import com.foxhunter.photo.entity.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 黑名单控制器实现类。
 *
 * @author Ewing
 */
@Controller
@RequestMapping("/blacklist")
@AuthLogin(needManager = true)
public class BlacklistController {
    private Logger logger = LoggerFactory.getLogger(BlacklistController.class);

    private BlacklistService blacklistService;

    @Autowired
    public void setBlacklistService(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    /**
     * 新增黑名单。
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addBlacklist(Blacklist blacklist, HttpServletRequest request) {
        try {
            Manager manager = (Manager) request.getSession().getAttribute(AppConfig.MANAGER_SESSION_KEY);
            Blacklist blacklistNew = blacklistService.addBlacklist(blacklist, manager);
            return ResultMessage.newSuccess("新增黑名单成功！").setData(blacklistNew);
        } catch (Exception e) {
            logger.error("新增黑名单异常：" + e.getMessage());
            return ResultMessage.newFailure("新增失败：" + e.getMessage());
        }
    }

    /**
     * 管理员带图片新增黑名单。
     */
    @ResponseBody
    @RequestMapping("/addWithImg")
    public ResultMessage addWithImg(@RequestParam(value = "file") MultipartFile file, Blacklist blacklist, HttpServletRequest request) {
        try {
            String path = request.getSession().getServletContext().getRealPath("upload");
            Manager manager = (Manager) request.getSession().getAttribute(AppConfig.MANAGER_SESSION_KEY);
            Blacklist blacklistNew = blacklistService.addWithImg(file.getInputStream(), blacklist, path, manager);
            return ResultMessage.newSuccess("新增黑名单成功！").setData(blacklistNew);
        } catch (Exception e) {
            logger.error("新增黑名单异常：" + e.getMessage());
            return ResultMessage.newFailure("新增失败：" + e.getMessage());
        }
    }

    /**
     * 根据来源图片查询黑名单。
     */
    @ResponseBody
    @RequestMapping("/listOfPhoto")
    public EasyUIGridData listBlacklistsOfPhoto(String photoId) {
        try {
            List<Blacklist> blacklists = blacklistService.listBlacklistsOfPhoto(photoId);
            return new EasyUIGridData(blacklists);
        } catch (Exception e) {
            logger.error("查询黑名单异常：" + e.getMessage());
            return new EasyUIGridData("name", "查询数据异常！");
        }
    }

    /**
     * 修改黑名单。
     */
    //@ResponseBody
    //@RequestMapping("/update")
    public ResultMessage updateBlacklist(Blacklist newBlacklist) {
        try {
            blacklistService.updateBlacklist(newBlacklist);
            return ResultMessage.newSuccess("修改黑名单成功！");
        } catch (Exception e) {
            logger.error("修改黑名单异常：" + e.getMessage());
            return ResultMessage.newFailure("修改黑名单失败！");
        }
    }

    /**
     * 管理员带图片修改黑名单。
     */
    //@ResponseBody
    //@RequestMapping("/updateWithImg")
    public ResultMessage updateWithImg(@RequestParam(value = "file") MultipartFile file, Blacklist blacklist, HttpServletRequest request) {
        try {
            String path = request.getSession().getServletContext().getRealPath("upload");
            Manager manager = (Manager) request.getSession().getAttribute(AppConfig.MANAGER_SESSION_KEY);
            blacklist.setAddManager(manager);
            blacklistService.updateWithImg(file.getInputStream(), blacklist, path, manager);
            return ResultMessage.newSuccess("修改黑名单成功！");
        } catch (Exception e) {
            logger.error("修改黑名单异常：" + e.getMessage());
            return ResultMessage.newFailure("修改失败：" + e.getMessage());
        }
    }

    /**
     * 查询黑名单。
     */
    @ResponseBody
    @RequestMapping("/list")
    public EasyUIGridData listBlacklists(int page, int rows, String name, String phoneNo) {
        try {
            Pageable pageable = new PageRequest(page - 1, rows);
            Page dataPage = blacklistService.listBlacklists(name, phoneNo, pageable);
            return new EasyUIGridData(dataPage);
        } catch (Exception e) {
            logger.error("查询黑名单异常：" + e.getMessage());
            return new EasyUIGridData("name", "查询数据异常！");
        }
    }

}
