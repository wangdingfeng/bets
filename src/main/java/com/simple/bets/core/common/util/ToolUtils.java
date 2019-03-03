package com.simple.bets.core.common.util;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ProjectName: better
 * @Package: com.simple.better.core.utils
 * @ClassName: ToolUtils
 * @Author: wangdingfeng
 * @Description: 工具类
 * @Date: 2018/12/24 10:16
 * @Version: 1.0
 */
public class ToolUtils {

    private static Logger logger = LoggerFactory.getLogger(ToolUtils.class);
    /**
     * 判断数组中是否存在此元素
     * @param str 元素
     * @param strs 数组
     * @return
     */
    public static boolean argsIsExitsStr(String str, List<String> strs){
        if (str != null && strs != null){
            for (String s : strs){
                if (str.equals(StringUtils.trim(s))){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断字符串中是否包含字段
     * @param a
     * @param str
     * @return
     */
    public static boolean StrinExitsStr(String a,String str){
        if(a.indexOf(str)>-1){
            return true;
        }
        return false;
    }
    /**
     * 对象是否不为空
     *
     * @author fengshuonan
     * @Date 2018/3/18 21:57
     */
    public static boolean isNotEmpty(Object o){
        return !isEmpty(o);
    }


    /**
     * 对象是否为空
     *
     * @author fengshuonan
     * @Date 2018/3/18 21:57
     */
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            if (o.toString().trim().equals("")) {
                return true;
            }
        } else if (o instanceof List) {
            if (((List) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Map) {
            if (((Map) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Set) {
            if (((Set) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Object[]) {
            if (((Object[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof int[]) {
            if (((int[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof long[]) {
            if (((long[]) o).length == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 翻译文件size
     * @param fileSize
     * @return
     */
    public static String readableFileSize(String fileSize) {
        long size = Long.valueOf(fileSize);
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.0#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 过滤空字符串
     * @param param
     * @return
     */
    public static Map<String,Object> filterEmptyParam(Map<String,Object> param){
        Map<String,Object> result = new HashMap<>();
        param = Maps.filterValues(param, s -> s != null && (!(s instanceof String) || !((String) s).isEmpty()));
        result.putAll(param);
        return result;
    }

    /**
     * 过滤空字符串
     * @param source
     * @param <T>
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     */
    public static <T> T setNullValue(T source){
        try {
            Field[] fields = source.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getGenericType().toString().equals(
                        "class java.lang.String") && !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                    field.setAccessible(true);
                    Object obj = field.get(source);
                    if (obj != null && obj.equals("")) {
                        field.set(source, null);
                    } else if (obj != null) {
                        String str = obj.toString();
                        field.set(source, str);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(" 过滤空字符串报错",e);
        }
        return source;
    }

    /**
     * 判断字符串是否是Long
     * @param str
     * @return
     */
    public static boolean isValidLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
