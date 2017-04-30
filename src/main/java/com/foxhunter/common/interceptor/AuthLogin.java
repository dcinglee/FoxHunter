package com.foxhunter.common.interceptor;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthLogin {
    // 默认为需要登陆。
    boolean needManager() default false;
    boolean needCustom() default false;
}