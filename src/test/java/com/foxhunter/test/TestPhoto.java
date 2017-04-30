package com.foxhunter.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxhunter.photo.dao.PhotoDao;
import com.foxhunter.photo.entity.Photo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 管理员测试类。
 *
 * @author Ewing
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestPhoto {
    private Logger logger = LoggerFactory.getLogger(TestPhoto.class);

    private PhotoDao photoDao;

    @Autowired
    public void setPhotoDao(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    @Test
    public void testQueryPhoto() throws JsonProcessingException {
        List<Photo> photos = photoDao.findAll((root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            return predicate;
        });
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writeValueAsString(photos);
        logger.info(jsonResult);
    }

    @Test
    public void testCountPhoto() throws JsonProcessingException {
        List<?> photos = photoDao.countByCreateCustomGroupByCheckState("ff80808159715f390159716467ac0000");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writeValueAsString(photos);
        logger.info(jsonResult);
    }

}
