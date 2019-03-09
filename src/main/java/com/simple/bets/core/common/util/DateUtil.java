package com.simple.bets.core.common.util;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    /**
     * 默认的时间格式化类型
     */
    public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式化类型
     */
    public final static String DATE_PATTERN_DETAIL = "yyyy-MM-dd hh mm ss";


    private DateUtil(){

    }

    private static String getDateFormat(Date date, String dateFormatType) {
        SimpleDateFormat simformat = new SimpleDateFormat(dateFormatType);
        return simformat.format(date);
    }

    public static String formatCSTTime(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date d = sdf.parse(date);
        return DateUtil.getDateFormat(d, format);
    }
    /**
     * 时间转成字符串
     *
     * @param date
     * @return
     */
    public static String date2Str(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat newstr = new SimpleDateFormat(pattern);
        return newstr.format(date);
    }

    /**
     * 时间转换成字符串-默认格式"yyyy-MM-dd"
     *
     * @param date
     * @return
     */
    public static String date2Str(Date date) {
        return date2Str(date, DEFAULT_DATE_PATTERN);
    }
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }
}
