package com.foxhunter.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxhunter.blacklist.dao.BlacklistDao;
import com.foxhunter.manager.dao.ManagerDao;
import com.foxhunter.manager.entity.Manager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 黑名单测试类。
 *
 * @author Ewing
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestBlacklist {
    private Logger logger = LoggerFactory.getLogger(TestBlacklist.class);

    private BlacklistDao blacklistDao;

    private ManagerDao managerDao;

    @Autowired
    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    @Autowired
    public void setBlacklistDao(BlacklistDao blacklistDao) {
        this.blacklistDao = blacklistDao;
    }

    @Test
    public void testQueryBlacklist() throws JsonProcessingException {
        Manager manager = managerDao.findFirstByName("123");
        Page page = blacklistDao.findByCheckedManager(manager, new PageRequest(0, 10));
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writeValueAsString(page);
        logger.info(jsonResult);
    }

}
