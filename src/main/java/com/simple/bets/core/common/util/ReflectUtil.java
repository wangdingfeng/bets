package com.simple.bets.core.common.util;

import cn.hutool.core.exceptions.UtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.core.common.util
 * @ClassName: ReflectUtil
 * @Author: wangdingfeng
 * @Description: ReflectUtil 工具扩展方法
 * @Date: 2019/3/12 18:32
 * @Version: 1.0
 */
public class ReflectUtil extends cn.hutool.core.util.ReflectUtil {

    private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

    /**
     * 重写反射调用内部方法
     * @param obj
     * @param methodName
     * @param args
     */
    public static void invokeMethod(Object obj, String methodName, Object... args){
        try {
            invoke(obj,methodName,args);
        } catch (UtilException e) {
            logger.error("调用"+methodName+"内部方式出错,请知晓");
        }
    }

    /**
     * 获取被注解的属性值 不重复注解
     * @param obj
     * @param annotationClass
     * @return
     */
    public static Object getAnnotationValue(Object obj,Class<? extends Annotation> annotationClass){
        Object value = null;
        Field[] field = getFields(obj.getClass());
        for(Field file : field){
            boolean fieldHasAnno =  file.isAnnotationPresent(annotationClass);
            if(!fieldHasAnno){
                continue;
            }
            value = getFieldValue(obj,file);
            break;
        }
        return value;
    }
}
