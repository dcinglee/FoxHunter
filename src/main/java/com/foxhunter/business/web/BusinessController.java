package com.foxhunter.business.web;

import com.foxhunter.business.entity.Dictionary;
import com.foxhunter.business.entity.Location;
import com.foxhunter.business.service.BusinessService;
import com.foxhunter.common.interceptor.AuthLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务控制器。
 *
 * @author Ewing
 */
@Controller
@RequestMapping("/business")
@AuthLogin(needManager = true)
public class BusinessController {
    private Logger logger = LoggerFactory.getLogger(BusinessController.class);

    private BusinessService businessService;

    @Autowired
    public void setBusinessService(BusinessService businessService) {
        this.businessService = businessService;
    }

    /**
     * 根据字典类型获取字典值。
     */
    @ResponseBody
    @RequestMapping("/dictionary/{type}")
    public List<Dictionary> dictionary(@PathVariable("type") String type) {
        try {
            return businessService.findDictionaryByType(type);
        } catch (Exception e) {
            logger.error("获取数据字典异常：" + e.getMessage());
            return new ArrayList<>(0);
        }
    }

    /**
     * 根据字典类型获取字典值。
     */
    @ResponseBody
    @RequestMapping("/location/{parent}")
    public List<Location> location(@PathVariable("parent") String parent) {
        try {
            return businessService.findLocationByParentId(parent);
        } catch (Exception e) {
            logger.error("获取地理位置数据异常：" + e.getMessage());
            return new ArrayList<>(0);
        }
    }

}
