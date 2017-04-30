package com.foxhunter.business.web;

import com.foxhunter.common.interceptor.AuthLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 控制页面访问。
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @RequestMapping("/{page}")
    @AuthLogin(needManager = true)
    public String page(@PathVariable("page") String page) {
        return page;
    }
}
