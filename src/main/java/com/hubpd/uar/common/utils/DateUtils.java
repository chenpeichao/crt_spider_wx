package com.hubpd.uar.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理工具类
 *
 * @author cpc
 * @create 2018-06-22 17:08
 **/
public class DateUtils {
    /**
     * 将日期字符串转换为指定格式的日期字符串
     * @param dateStr               日期字符串
     * @param sourcePattern         日期字符串对应格式
     * @param targetPattern         日期字符串转换后期望的格式
     * @return
     */
    public static String parseDateStrByPattern(String dateStr, String sourcePattern, String targetPattern) throws ParseException {
        Date sourcePatternDate = new SimpleDateFormat(sourcePattern).parse(dateStr);
        return new SimpleDateFormat(targetPattern).format(sourcePatternDate);
    }

    /**
     * 将日期转换为指定格式的日期字符串
     * @param sourceDate            日期
     * @param pattern               日期字符串转换后期望的格式
     * @return
     */
    public static String parseDate2StringByPattern(Date sourceDate, String pattern) {
        return new SimpleDateFormat(pattern).format(sourceDate);
    }

    /**
     * 将日期转换为指定格式的日期字符串
     * @param sourceDataStr         带转换日期字符串
     * @param pattern               日期字符串转换后期望的格式
     * @return
     */
    public static Date parseDateStr2DateByPattern(String sourceDataStr, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(sourceDataStr);
    }
}
