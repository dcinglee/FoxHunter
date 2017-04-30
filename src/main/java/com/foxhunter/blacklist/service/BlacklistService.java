package com.foxhunter.blacklist.service;

import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.manager.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 黑名单服务接口。
 *
 * @author Ewing
 */
public interface BlacklistService {

    Page<Blacklist> listBlacklists(String name, String phoneNo, Pageable pageable);

    /**
     * 根据联系方式查询黑名单。
     */
    List<Blacklist> findByContact(Blacklist blacklist);

    Blacklist validateAndResolve(Blacklist blacklist);

    Blacklist addBlacklist(Blacklist blacklist, Manager manager);

    Blacklist updateBlacklist(Blacklist blacklist);

    Blacklist addWithImg(InputStream inStream, Blacklist blacklist, String path, Manager manager) throws IOException;

    Blacklist updateWithImg(InputStream inStream, Blacklist blacklist, String path, Manager manager) throws IOException;

    List<Blacklist> listBlacklistsOfPhoto(String photoId);

    long countBlacklistsOfPhoto(String photoId);
}
