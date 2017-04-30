package com.foxhunter.blacklist.dao;

import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.manager.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 黑名单数据库访问类。
 *
 * @author Ewing
 */
public interface BlacklistDao extends JpaRepository<Blacklist, String>, JpaSpecificationExecutor<Blacklist> {

    Page<Blacklist> findByCheckedManager(Manager manager, Pageable pageable);

    List<Blacklist> findByFromPhotoPhotoId(String photoId);

    long countByFromPhotoPhotoId(String photoId);

    List<Blacklist> findByPhoneNoOrQqNoOrMicroNo(String phoneNo, String qqNo, String microNo);
}
