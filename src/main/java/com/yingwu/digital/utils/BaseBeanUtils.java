package com.yingwu.digital.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Created by: zhangbingbing
 * @date 2018/9/6
 **/
public class BaseBeanUtils {
    /**
     * 判断对象是否为空或空值、空格
     *
     * @param value
     * @return
     */
    public static boolean isNull(Object value) {
        boolean result = false;
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            result = StringUtils.isBlank((String) value);
        }
        return result;
    }

    /**
     * 判断对象是否不为空或空值、空格
     *
     * @param value
     * @return
     */
    public static boolean isNotNull(Object value) {
        return !isNull(value);
    }

    /**
     * 判断list是否为null、size为0
     *
     * @param list
     * @return
     */
    public static boolean listIsNull(List list) {
        if (list == null) {
            return true;
        }
        if (list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串为空，为空返回true，不为空返回false
     *
     * @param str
     * @return
     */
    public static boolean stringEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 判断字符串非空，非空返回true，为空返回false
     *
     * @param str
     * @return
     * @throws
     */
    public static boolean stringNotEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 拷贝属性
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    /**
     * 拷贝属性
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    /**
     * 将源对象中的值覆盖到目标对象中，仅覆盖源对象中不为NULL值的属性
     *
     * @param source
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 判断两个对象是否相等
     *
     * @param value1
     * @param value2
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static boolean comparValue(Object value1, Object value2) {
        if (isNull(value1) && isNull(value2)) {
            return true;
        }
        if (isNull(value1) || isNull(value2)) {
            return false;
        }
        if (value1 instanceof Comparable) {
            return (((Comparable) value1).compareTo((Comparable) value2) == 0);
        } else {
            return value1.equals(value2);
        }
    }
}
