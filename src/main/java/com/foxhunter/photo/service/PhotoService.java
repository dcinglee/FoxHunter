package com.foxhunter.photo.service;

import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.manager.entity.Manager;
import com.foxhunter.photo.entity.Photo;
import com.foxhunter.photo.web.PhotoVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * 照片服务层
 *
 * @author lijing
 */
public interface PhotoService {
    //增加、修改方法
    public Photo addOrUpdate(Photo photo);

    //得到图片列表
    public Page<Photo> findPhotosByCustomAndState(String customName, Integer checkState, Pageable pageable);

    public Page<Photo> listPhotosOfCustom(String openId, Integer checkState, Pageable pageable);

    Photo uploadPhoto(MultipartFile file, PhotoVo photoVo, String path) throws IOException;

    ResultMessage auditPassAddBlacklist(Photo photoAudit, Blacklist blacklist, Manager manager);

    ResultMessage auditPhotoNo(Photo photoAudit, Manager manager);

    Photo findNextPhoto(String photoId, Integer nextState);
}
