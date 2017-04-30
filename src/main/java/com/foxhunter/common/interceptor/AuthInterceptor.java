package com.foxhunter.common.interceptor;

import com.foxhunter.common.config.AppConfig;
import com.foxhunter.common.util.EHCacheUtil;
import com.foxhunter.custom.entity.Custom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    private static final String ajaxFail = "{\"code\":2,\"success\":false,\"data\":\"\",\"message\":\"您尚登陆或会话超时！\"}";
    private final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    /**
     * 在业务处理器处理请求之前被调用。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthLogin authMethod = ((HandlerMethod) handler).getMethodAnnotation(AuthLogin.class);
        boolean needManager;
        boolean needCustom;
        if (authMethod != null) { // 方法上的注解优先。
            needManager = authMethod.needManager();
            needCustom = authMethod.needCustom();
        } else { // 方法上没有注解才考虑类上的注解。
            AuthLogin authClass = ((HandlerMethod) handler).getBeanType().getAnnotation(AuthLogin.class);
            needManager = authClass != null && authClass.needManager();
            needCustom = authClass != null && authClass.needCustom();
        }
        // 需要管理员登陆
        if ((needManager && request.getSession().getAttribute(AppConfig.MANAGER_SESSION_KEY) == null)) {
            logger.info("有未登陆的管理员请求来自：" + request.getRemoteHost());
            dealFailRequest(request, response);
            return false;
        }
        // 需要微信客户登陆
        if (needCustom) {
            String openId = request.getParameter("openId");
            String customId = request.getParameter("customId");
            if (openId == null && customId == null) {
                logger.info("有未授权的客户请求来自：" + request.getRemoteHost());
                dealFailRequest(request, response);
                return false;
            }
            Custom openCustom = EHCacheUtil.get(openId);
            Custom custom = EHCacheUtil.get(customId);
            if ((openCustom == null || openCustom.getCustomId() == null)
            		&& (custom == null || custom.getCustomId() == null)) {
                logger.info("有未登陆的客户请求来自：" + request.getRemoteHost());
                dealFailRequest(request, response);
                return false;
            }
        }
        return true;
    }

    /**
     * 处理鉴权失败的请求，异步调用返回Json，普通请求转到登陆页面。
     */
    private void dealFailRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("X-Requested-With") != null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/javascript;charset=utf-8");
            response.getWriter().write(ajaxFail);
        } else {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }

}

