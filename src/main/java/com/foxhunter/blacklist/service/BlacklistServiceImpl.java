package com.foxhunter.blacklist.service;

import com.foxhunter.blacklist.dao.BlacklistDao;
import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.business.entity.Location;
import com.foxhunter.common.config.AppConfig;
import com.foxhunter.common.interceptor.AuthLogin;
import com.foxhunter.manager.dao.ManagerDao;
import com.foxhunter.manager.entity.Manager;
import com.foxhunter.photo.dao.PhotoDao;
import com.foxhunter.photo.entity.Photo;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.persistence.criteria.Predicate;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 黑名单服务实现类。
 *
 * @author Ewing
 */
@Service
@AuthLogin(needManager = true)
public class BlacklistServiceImpl implements BlacklistService {
    private Logger logger = LoggerFactory.getLogger(BlacklistServiceImpl.class);

    private BlacklistDao blacklistDao;
    private PhotoDao photoDao;
    private ManagerDao managerDao;

    @Autowired
    public void setBlacklistDao(BlacklistDao blacklistDao) {
        this.blacklistDao = blacklistDao;
    }

    @Autowired
    public void setPhotoDao(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    @Autowired
    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    public Blacklist validateAndResolve(Blacklist blacklist) {
        if (!StringUtils.hasText(blacklist.getName())) {
            throw new RuntimeException("黑名单名称不能为空！");
        }
        boolean none = true;
        // 校验电话号码
        String phoneNo = blacklist.getPhoneNo();
        if (StringUtils.hasText(phoneNo)) {
            Matcher matcher = AppConfig.PHONE_NO_PATTERN.matcher(phoneNo);
            if (matcher.find()) {
                phoneNo = matcher.group();
                blacklist.setPhoneNo(phoneNo);
                none = false;
            }
        }
        // 校验QQ号码
        String qqNo = blacklist.getQqNo();
        if (StringUtils.hasText(qqNo)) {
            Matcher matcher = AppConfig.QQ_NO_PATTERN.matcher(qqNo);
            if (matcher.find()) {
                qqNo = matcher.group();
                blacklist.setQqNo(qqNo);
                none = false;
            }
        }
        // 校验微信号码
        String microNo = blacklist.getMicroNo();
        if (StringUtils.hasText(microNo)) {
            Matcher matcher = AppConfig.MICRO_NO_PATTERN.matcher(microNo);
            if (matcher.find()) {
                microNo = matcher.group();
                blacklist.setMicroNo(microNo);
                none = false;
            }
        }
        if (none) {
            throw new RuntimeException("至少需要一项联系方式！");
        }
        // 字典值处理
        if (blacklist.getOrgTypeValue() == null)
            blacklist.setOrgTypeValue(1);
        if (blacklist.getStateValue() == null)
            blacklist.setStateValue(1);
        if (blacklist.getLevelValue() == null)
            blacklist.setLevelValue(1);
        if (blacklist.getFromTypeValue() == null)
            blacklist.setFromTypeValue(201);
        // 来源图片处理
        if (blacklist.getFromPhoto() == null && StringUtils.hasText(blacklist.getFromPhotoId()))
            blacklist.setFromPhoto(new Photo(blacklist.getFromPhotoId()));
        // 所在省份处理
        if (blacklist.getProvince() == null && StringUtils.hasText(blacklist.getProvinceId()))
            blacklist.setProvince(new Location(blacklist.getProvinceId()));
        // 所在城市处理
        if (blacklist.getCity() == null && StringUtils.hasText(blacklist.getCityId()))
            blacklist.setCity(new Location(blacklist.getCityId()));
        // 所在区县处理
        if (blacklist.getCounty() == null && StringUtils.hasText(blacklist.getCountyId()))
            blacklist.setCounty(new Location(blacklist.getCountyId()));
        return blacklist;
    }

    public List<Blacklist> findByContact(Blacklist blacklist) {
        if (!StringUtils.hasText(blacklist.getPhoneNo())
                && !StringUtils.hasText(blacklist.getQqNo())
                && !StringUtils.hasText(blacklist.getMicroNo())) {
            return new ArrayList<>(0);
        }
        Specification<Blacklist> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.hasText(blacklist.getPhoneNo())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("phoneNo"), blacklist.getPhoneNo()));
            }
            if (StringUtils.hasText(blacklist.getQqNo())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("qqNo"), blacklist.getQqNo()));
            }
            if (StringUtils.hasText(blacklist.getMicroNo())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("microNo"), blacklist.getMicroNo()));
            }
            return predicate;
        };
        return blacklistDao.findAll(specification);
    }

    @Override
    public Page<Blacklist> listBlacklists(String name, String phoneNo, Pageable pageable) {
        Specification<Blacklist> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction(); // 创建且(and)条件，初始为1=1。
            if (StringUtils.hasText(name)) { // 名称不空白，添加到且条件。
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + name + "%"));
            }
            if (StringUtils.hasText(phoneNo)) { // 电话号码不空白，添加到且条件。
                predicate = builder.and(predicate, builder.like(root.get("phoneNo"), "%" + phoneNo + "%"));
            }
            return predicate;
        };
        return blacklistDao.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public Blacklist addBlacklist(Blacklist blacklist, Manager manager) {
        // 验证并防止联系方式重复
        blacklist = validateAndResolve(blacklist);
        List<Blacklist> blacklists = findByContact(blacklist);
        if (blacklists != null && !blacklists.isEmpty()) {
            throw new RuntimeException("该联系方式的组合已存在！");
        }
        // 管理员添加的黑名单数增加。
        manager.setAddBlacklistNum(manager.getAddBlacklistNum() + 1);
        managerDao.save(manager);
        // 保存黑名单。
        blacklist.setCreateDate(new Date());
        blacklist.setAddManager(manager);
        return blacklistDao.save(blacklist);
    }

    @Override
    @Transactional
    public Blacklist updateBlacklist(Blacklist blacklist) {
        // 验证并防止联系方式重复
        blacklist = validateAndResolve(blacklist);
        List<Blacklist> blacklists = findByContact(blacklist);
        if (blacklists != null && !blacklists.isEmpty()) {
            throw new RuntimeException("该联系方式的组合已存在！");
        }
        // 查出要修改的对象。
        Blacklist oldBlacklist = blacklistDao.findOne(blacklist.getBlacklistId());
        if (oldBlacklist == null) {
            throw new RuntimeException("该黑名单不存在或已删除！");
        }
        // 更新黑名单，对需要修改的字段做控制，不能使用BeanUtil工具复制全部字段。
        oldBlacklist.setName(blacklist.getName());
        oldBlacklist.setLevelValue(blacklist.getLevelValue());
        oldBlacklist.setOrgTypeValue(blacklist.getOrgTypeValue());
        oldBlacklist.setStateValue(blacklist.getStateValue());
        oldBlacklist.setPhoneNo(blacklist.getPhoneNo());
        oldBlacklist.setIdCardNo(blacklist.getIdCardNo());
        oldBlacklist.setBankCards(blacklist.getBankCards());
        oldBlacklist.setLongitude(blacklist.getLongitude());
        oldBlacklist.setLatitude(blacklist.getLatitude());
        oldBlacklist.setAddress(blacklist.getAddress());
        oldBlacklist.setFromTypeValue(blacklist.getFromTypeValue());
        oldBlacklist.setFromInfo(blacklist.getFromInfo());
        if (blacklist.getFromPhoto() != null) {
            oldBlacklist.setFromPhoto(blacklist.getFromPhoto());
        }
        return blacklistDao.save(oldBlacklist);
    }

    @Override
    @Transactional
    public Blacklist addWithImg(InputStream inStream, Blacklist blacklist, String path, Manager manager) throws IOException {
        Thumbnails.Builder imgBuilder = Thumbnails.of(inStream)
                .size(AppConfig.PHOTO_MAX_WIDTH, AppConfig.PHOTO_MAX_HEIGHT);
        BufferedImage image = imgBuilder.asBufferedImage();
        //设置图片属性。
        Photo newPhoto = new Photo();
        newPhoto.setWidth(image.getWidth());
        newPhoto.setHeight(image.getHeight());
        newPhoto.setCreateManager(manager);
        newPhoto.setCreateDate(new Date());
        newPhoto.setFormat(AppConfig.PHOTO_FORMAT);
        newPhoto.setCheckState(5);//5表示管理员录入
        Photo photo = photoDao.save(newPhoto);
        //获得文件名使文件名与图片ID一致以防重复。
        String fileName = photo.getPhotoId() + "." + AppConfig.PHOTO_FORMAT;
        photo.setPath("/upload/" + fileName);
        photo.setFileName(fileName);
        photo = photoDao.save(photo);
        //更新黑名单信息。
        blacklist.setFromPhoto(photo);
        Blacklist newBlacklist = addBlacklist(blacklist, manager);
        //最后保存图片，因为文件不被事务控制。
        ImageIO.write(image, AppConfig.PHOTO_FORMAT, new File(path + "/" + fileName));
        return newBlacklist;
    }

    @Override
    @Transactional
    public Blacklist updateWithImg(InputStream inStream, Blacklist blacklist, String path, Manager manager) throws IOException {
        Thumbnails.Builder imgBuilder = Thumbnails.of(inStream)
                .size(AppConfig.PHOTO_MAX_WIDTH, AppConfig.PHOTO_MAX_HEIGHT);
        BufferedImage image = imgBuilder.asBufferedImage();
        //设置图片属性。
        Photo newPhoto = new Photo();
        newPhoto.setWidth(image.getWidth());
        newPhoto.setHeight(image.getHeight());
        newPhoto.setCreateManager(manager);
        newPhoto.setCreateDate(new Date());
        newPhoto.setFormat(AppConfig.PHOTO_FORMAT);
        newPhoto.setCheckState(5);//5表示管理员录入
        Photo photo = photoDao.save(newPhoto);
        //获得文件名使文件名与图片ID一致以防重复。
        String fileName = photo.getPhotoId() + "." + AppConfig.PHOTO_FORMAT;
        photo.setPath("/upload/" + fileName);
        photo.setFileName(fileName);
        photoDao.save(photo);
        //更新黑名单信息，此处不需要修改新增管理员属性。
        blacklist.setFromPhoto(photo);
        Blacklist newBlacklist = updateBlacklist(blacklist);
        //最后保存图片，因为文件不被事务控制。
        ImageIO.write(image, AppConfig.PHOTO_FORMAT, new File(path + "/" + fileName));
        return newBlacklist;
    }

    @Override
    public List<Blacklist> listBlacklistsOfPhoto(String photoId) {
        return blacklistDao.findByFromPhotoPhotoId(photoId);
    }

    @Override
    public long countBlacklistsOfPhoto(String photoId) {
        return blacklistDao.countByFromPhotoPhotoId(photoId);
    }

}
