package com.foxhunter.common.util;

import com.foxhunter.common.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据模型、日期、对象与字符等类型转换。
 *
 * @author Ewing
 */
public class Converter {
    public final static SimpleDateFormat DATE_FORMATER = new SimpleDateFormat(AppConfig.DATE_FORMAT);
    public final static SimpleDateFormat DATE_TIME_FORMATER = new SimpleDateFormat(AppConfig.DATE_TIME_FORMAT);
    private static Logger logger = LoggerFactory.getLogger(Converter.class);

    public static Date StringToDate(String dateStr) {
        Date date = null;
        if (dateStr != null && dateStr.length() > 0) {
            try {
                date = DATE_FORMATER.parse(dateStr);
            } catch (ParseException e) {
                logger.error("日期转换异常：" + e.getMessage());
            }
        }
        return date;
    }

    public static Date StringToDateTime(String dateStr) {
        Date date = null;
        if (dateStr != null && dateStr.length() > 0) {
            try {
                date = DATE_TIME_FORMATER.parse(dateStr);
            } catch (ParseException e) {
                logger.error("日期时间转换异常：" + e.getMessage());
            }
        }
        return date;
    }

}
