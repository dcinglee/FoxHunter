package com.foxhunter.blacklist.service;

import com.foxhunter.blacklist.entity.BlackQQGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 *  QQ群服务接口。
 */
public interface BlackQQGroupService {

    BlackQQGroup addQQGroup(BlackQQGroup blackQQGroup);

    Page<BlackQQGroup> listBlackQQGroups(String groupNo, String myQQ, Pageable pageable);

    BlackQQGroup updateQQGroup(BlackQQGroup blackQQGroup);

}
