package com.foxhunter.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxhunter.manager.dao.ManagerDao;
import com.foxhunter.manager.entity.Manager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 管理员测试类。
 *
 * @author Ewing
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestManager {
    private Logger logger = LoggerFactory.getLogger(TestManager.class);

    private ManagerDao managerDao;

    @Autowired
    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    @Test
    public void testQueryManager() throws JsonProcessingException {
        List<Manager> managers = managerDao.findAll(new Specification<Manager>() {
            @Override
            public Predicate toPredicate(Root<Manager> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.like(root.get("name"), "%好%");
                return predicate;
            }
        });
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writeValueAsString(managers);
        logger.info(jsonResult);
    }

}
