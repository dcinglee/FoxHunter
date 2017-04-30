package com.foxhunter.common.config;

import java.util.regex.Pattern;

/**
 * 统一全局配置。
 *
 * @author Ewing
 */
public final class AppConfig {

    // 微信授权认证配置
    public static final String WEIXIN_APPID = "wx2843d4d355f3645e";
    public static final String WEIXIN_SECRET = "25dd7b1b4c307b9116de4a47f362bd41";
    
    public static final String WEIXIN_ANDROID_ID = "wxb53ad9fb0db865f6";
    public static final String WEIXIN_ANDROID_SECRET = "8563ec8ae6b09d72ee4e9089f5a9d1df";
    
    public static final int CONNECT_TIME_OUT = 10000;

    // 统一日期时间格式
    public static final String TIMEZONE = "GMT+8";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_MILLIS_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    // 图片最大尺寸
    public static final int PHOTO_MAX_WIDTH = 800;
    public static final int PHOTO_MAX_HEIGHT = 800;
    public static final String PHOTO_FORMAT = "jpg";

    // 每张图片的收益
    public static final Float INCOME_PER_PHOTO = 0.5f;

    // 最小提现金额
    public static final float MIN_WITHDRAW_CASH = 0.1f;

    // 管理员SessionKey
    public static final String MANAGER_SESSION_KEY = "loginManager";
    // 客户的SessionKey
    public static final String CUSTOM_SESSION_KEY = "authCustom";

    // 单个手机号、QQ号、微信号的正则
    public static final String PHONE_NO_REGEX = "\\+?\\d{5,20}";
    public static final String QQ_NO_REGEX = "\\d{5,20}";
    public static final String MICRO_NO_REGEX = "[a-zA-Z\\d_]{5,64}";
    public static final Pattern PHONE_NO_PATTERN = Pattern.compile(PHONE_NO_REGEX);
    public static final Pattern QQ_NO_PATTERN = Pattern.compile(QQ_NO_REGEX);
    public static final Pattern MICRO_NO_PATTERN = Pattern.compile(MICRO_NO_REGEX);

    // 客户端查询黑名单每条次的价格
    public static final float QUERY_PRISE_PER_BLACKLIST = 0.01f;
}
