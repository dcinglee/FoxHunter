package com.foxhunter.photo.web;

import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.common.config.AppConfig;
import com.foxhunter.common.interceptor.AuthLogin;
import com.foxhunter.common.vo.EasyUIGridData;
import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.manager.entity.Manager;
import com.foxhunter.photo.entity.Photo;
import com.foxhunter.photo.service.PhotoService;
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

/**
 * 图片控制层
 *
 * @author lijing
 * @2016年12月13日上午11:59:21
 */

@Controller
@RequestMapping("/photo")
public class PhotoController {
    private Logger logger = LoggerFactory.getLogger(PhotoController.class);

    private PhotoService photoService;//照片

    @Autowired
    public void setPhotoService(PhotoService photoService) {
        this.photoService = photoService;
    }

    /**
     * 客户上传图片的方法。
     */
    @ResponseBody
    @RequestMapping("/upload")
    @AuthLogin(needCustom = true)
    public ResultMessage uploadImg(@RequestParam(value = "file") MultipartFile file, PhotoVo photoVo, HttpServletRequest request) {
        try {
            String path = request.getSession().getServletContext().getRealPath("upload");
            photoService.uploadPhoto(file, photoVo, path);
            return ResultMessage.newSuccess("上传图片成功！");
        } catch (Exception e) {
            logger.error("上传图片异常：" + e.getMessage());
            return ResultMessage.newFailure("上传图片失败！");
        }
    }

    /**
     * 修改方法。
     */
    @ResponseBody
    @RequestMapping("/update")
    @AuthLogin(needManager = true)
    public ResultMessage update(Photo photo) {
        try {
            photoService.addOrUpdate(photo);
            return ResultMessage.newSuccess("修改图片成功！");
        } catch (Exception e) {
            logger.error("修改图片异常：" + e.getMessage());
            return ResultMessage.newFailure("修改图片失败！");
        }
    }

    /**
     * 图片审核通过的方法。
     */
    @ResponseBody
    @RequestMapping("/auditPass")
    @AuthLogin(needManager = true)
    public ResultMessage auditPassAddBlacklist(Photo photoAudit, Blacklist blacklist, HttpServletRequest request) {
        try {
            Manager manager = (Manager) request.getSession().getAttribute(AppConfig.MANAGER_SESSION_KEY);
            return photoService.auditPassAddBlacklist(photoAudit, blacklist, manager);
        } catch (Exception e) {
            logger.error("审核图片异常：" + e.getMessage());
            return ResultMessage.newFailure("图片审核失败：" + e.getMessage());
        }
    }

    /**
     * 图片审核不通过的方法。
     */
    @ResponseBody
    @RequestMapping("/auditNo")
    @AuthLogin(needManager = true)
    public ResultMessage auditNo(Photo photoAudit, HttpServletRequest request) {
        try {
            Manager manager = (Manager) request.getSession().getAttribute(AppConfig.MANAGER_SESSION_KEY);
            return photoService.auditPhotoNo(photoAudit, manager);
        } catch (Exception e) {
            logger.error("审核图片异常：" + e.getMessage());
            return ResultMessage.newFailure("图片审核失败：" + e.getMessage());
        }
    }

    /**
     * 得到图片列表的方法
     */
    @ResponseBody
    @RequestMapping("/list")
    @AuthLogin(needManager = true)
    public EasyUIGridData listPhotos(int page, int rows, String customName, Integer checkState) {
        try {
            Pageable pageable = new PageRequest(page - 1, rows);
            Page dataPage = photoService.findPhotosByCustomAndState(customName, checkState, pageable);
            return new EasyUIGridData(dataPage);
        } catch (Exception e) {
            logger.error("查询图片异常：" + e.getMessage());
            return new EasyUIGridData("fileName", "查询图片异常！");
        }
    }

    /**
     * 获取下一张图片。
     */
    @ResponseBody
    @RequestMapping("/next")
    @AuthLogin(needManager = true)
    public ResultMessage nextPhoto(String photoId, Integer nextState) {
        try {
            if (!StringUtils.hasText(photoId)) {
                return ResultMessage.newFailure("图片ID不能空！");
            }
            Photo photo = photoService.findNextPhoto(photoId, nextState);
            if (photo == null) {
                return ResultMessage.newFailure("未找到下一张图片！");
            }
            return ResultMessage.newSuccess().setData(photo);
        } catch (Exception e) {
            logger.error("查找下一张图片出错：" + e.getMessage());
            return ResultMessage.newFailure("查找下一张图片出错！");
        }
    }

}
