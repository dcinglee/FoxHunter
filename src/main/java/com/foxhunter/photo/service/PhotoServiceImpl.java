package com.foxhunter.photo.service;

import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.blacklist.service.BlacklistService;
import com.foxhunter.common.config.AppConfig;
import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.custom.dao.CustomDao;
import com.foxhunter.custom.entity.Custom;
import com.foxhunter.manager.dao.ManagerDao;
import com.foxhunter.manager.entity.Manager;
import com.foxhunter.photo.dao.PhotoDao;
import com.foxhunter.photo.entity.Photo;
import com.foxhunter.photo.web.PhotoVo;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.criteria.Predicate;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * 照片服务实现类
 *
 * @author lijing
 * @2016年12月14日上午11:47:57
 */
@Service
public class PhotoServiceImpl implements PhotoService {
    private Logger logger = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private PhotoDao photoDao;

    private BlacklistService blacklistService;

    private CustomDao customDao;

    private ManagerDao managerDao;

    @Autowired
    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    @Autowired
    public void setCustomDao(CustomDao customDao) {
        this.customDao = customDao;
    }

    @Autowired
    public void setBlacklistService(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @Autowired
    public void setPhotoDao(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    @Override
    @Transactional
    public Photo addOrUpdate(Photo photo) {
        return photoDao.save(photo);
    }

    @Override
    @Transactional
    public Photo uploadPhoto(MultipartFile file, PhotoVo photoVo, String path) throws IOException {
        String openId = photoVo.getOpenId();
        Custom custom = customDao.findFirstByOpenId(openId);
        if (custom == null) {
            throw new RuntimeException("未找到上传客户！");
        }
        if (custom.getStateValue() == 2) {
            throw new RuntimeException("您的用户被锁定，请联系管理员。");
        }
        //裁剪图片大小（保持比例）
        Thumbnails.Builder imgBuilder = Thumbnails.of(file.getInputStream())
                .size(AppConfig.PHOTO_MAX_WIDTH, AppConfig.PHOTO_MAX_HEIGHT);
        BufferedImage image = imgBuilder.asBufferedImage();
        //客户上传的图片数增加
        custom.setAllPhotoNum(custom.getAllPhotoNum() + 1);
        customDao.save(custom);
        Photo newPhoto = new Photo();
        newPhoto.setLongitude(photoVo.getLongitude());
        newPhoto.setLatitude(photoVo.getLatitude());
        newPhoto.setWidth(image.getWidth());
        newPhoto.setHeight(image.getHeight());
        newPhoto.setCreateCustom(custom);
        newPhoto.setCreateDate(new Date());
        newPhoto.setFormat(AppConfig.PHOTO_FORMAT);
        newPhoto.setCheckState(1); //1表示未审核
        Photo photo = photoDao.save(newPhoto);
        String fileName = photo.getPhotoId() + "." + AppConfig.PHOTO_FORMAT;
        photo.setPath("/upload/" + fileName);
        photo.setFileName(fileName);
        Photo aPhoto = photoDao.save(photo);
        //最后保存图片，因为文件不被事务控制。
        ImageIO.write(image, AppConfig.PHOTO_FORMAT, new File(path + "/" + fileName));
        return aPhoto;
    }

    @Override
    public ResultMessage auditPassAddBlacklist(Photo photoAudit, Blacklist blacklist, Manager manager) {
        if (!StringUtils.hasText(photoAudit.getPhotoId())) {
            return ResultMessage.newFailure("图片ID不能为空！");
        }
        Photo photo = photoDao.findOne(photoAudit.getPhotoId());
        if (photo == null) {
            return ResultMessage.newFailure("该图片不存在或已删除！");
        }
        blacklist.setFromPhoto(photo);
        blacklist = blacklistService.addBlacklist(blacklist, manager);
        if (photo.getCheckState() != 2) {
            photo.setAuditInfo(photoAudit.getAuditInfo());
            photo.setCheckState(2); //图片审核通过
            photo.setAuditManager(manager);
            photo.setAuditDate(new Date());
            photoDao.save(photo);
        }
        Custom custom = photo.getCreateCustom();
        if (custom != null) { // 客户被审核过图片增加、提供的黑名单数增加、收益增加
            custom.setCheckedPhotoNum(custom.getCheckedPhotoNum() + 1);
            custom.setBlacklistNum(custom.getBlacklistNum() + 1);
            custom.setIncome(custom.getIncome() + AppConfig.INCOME_PER_PHOTO);
            customDao.save(custom);
        }
        // 管理员审核过的图片数增加
        manager.setCheckedPhotoNum(manager.getCheckedPhotoNum() + 1);
        managerDao.save(manager);
        return ResultMessage.newSuccess("审核通过成功！").setData(blacklist);
    }

    @Override
    public Photo findNextPhoto(String photoId, Integer nextState) {
        Photo photo = photoDao.findOne(photoId);
        return photoDao.findFirstByCheckStateAndCreateDateGreaterThanOrderByCreateDateAsc(nextState, photo.getCreateDate());
    }

    @Override
    public Page<Photo> findPhotosByCustomAndState(String customName, Integer checkState, Pageable pageable) {
        return photoDao.findAll((root, query, builder) -> {
            Predicate predicate = builder.conjunction();// 创建且(and)条件，初始为1=1。
            if (StringUtils.hasText(customName)) {// 名称不空白，添加到且条件。
                predicate = builder.and(predicate, builder.like(root.get("createCustom").get("name"), "%" + customName + "%"));
            }
            if (checkState != null) {
                predicate = builder.and(predicate, builder.equal(root.get("checkState"), checkState));
            }
            query.orderBy(builder.asc(root.get("createDate")));
            return predicate;
        }, pageable);
    }

    @Override
    @Transactional
    public ResultMessage auditPhotoNo(Photo photoAudit, Manager manager) {
        String photoId = photoAudit.getPhotoId();
        if (!StringUtils.hasText(photoId)) {
            return ResultMessage.newFailure("图片ID不能为空！");
        }
        Photo photo = photoDao.findOne(photoAudit.getPhotoId());
        if (photo == null) {
            return ResultMessage.newFailure("该图片不存在或已删除！");
        }
        if (photo.getCheckState() == 2 || blacklistService.countBlacklistsOfPhoto(photoId) > 0) {
            return ResultMessage.newFailure("审核不通过失败：该图片已审核通过或已录入黑名单！");
        }
        photo.setAuditInfo(photoAudit.getAuditInfo());
        photo.setAuditDate(new Date());
        photo.setAuditManager(manager);
        photo.setCheckState(3); //审核不通过
        photoDao.save(photo);
        // 管理员审核过的图片数增加
        manager.setCheckedPhotoNum(manager.getCheckedPhotoNum() + 1);
        managerDao.save(manager);
        return ResultMessage.newSuccess("审核不通过成功！");
    }

    @Override
    public Page<Photo> listPhotosOfCustom(String openId, Integer checkState, Pageable pageable) {
        if (null != openId && !"".equals(openId)) {
            Custom custom = customDao.findFirstByOpenId(openId);
            if (null != custom) {
                return photoDao.findByCreateCustomCustomIdAndCheckStateOrderByCreateDateDesc(custom.getCustomId(), checkState, pageable);
            }
        }
        return new PageImpl<>(new ArrayList<>(0));
    }

}
