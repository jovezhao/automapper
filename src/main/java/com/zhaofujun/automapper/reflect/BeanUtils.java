package com.zhaofujun.automapper.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeanUtils {

    //获取bean中所有字段
    public static Field[] getAllFields(Class clazz) {
        return clazz.getDeclaredFields();
    }

    public static Field getField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    //获取bean中字段的setter方法
    public static Method getSetter(Field field) {

        if(field==null) return null;

        try {
            Class fieldType = field.getType();// 返回参数类型
            StringBuilder sb = new StringBuilder();
            sb.append("set").append(field.getName().substring(0, 1).toUpperCase())
                    .append(field.getName().substring(1));
            Method method = field.getDeclaringClass().getMethod(sb.toString(), fieldType);
            return method;
        } catch (Exception ex) {
            return null;
        }
    }

    //获取bean中字段的getter方法
    public static Method getGetter(Field field) {
        if(field==null) return null;
        try {
            Class fieldType = field.getType();// 返回参数类型
            StringBuilder sb = new StringBuilder();
            if (fieldType.equals(boolean.class)) {
                sb.append("is").append(field.getName().substring(0, 1).toUpperCase())
                        .append(field.getName().substring(1));
            } else {
                sb.append("get").append(field.getName().substring(0, 1).toUpperCase())
                        .append(field.getName().substring(1));
            }
            Method method = field.getDeclaringClass().getMethod(sb.toString());
            return method;
        } catch (Exception ex) {
            return null;
        }
    }



}
