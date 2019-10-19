package com.zhaofujun.automapper.reflect;

import com.zhaofujun.automapper.converters.TypeManager;

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


    public static Object parseValue(Object value, Class valueClass, Class targetClass) throws Exception {
        // 先判断两个类型是否一致，如果一致，直接使用
        if (valueClass.equals(targetClass))
            return value;

        // 如果目标是字符串，直接使用tostring返回
        if (targetClass.equals(String.class))
            return value.toString();

        // 如果目标类型是源类型的包装器,通过包装器类型的valueOf静态方法创建对象
        if (targetClass.equals(TypeManager.getWrapperClass(valueClass)))
            return targetClass.getDeclaredMethod("valueOf",valueClass).invoke(null,value);

        // 如果源类类型是包装器，目标类型是基础类型，可以直接返回
        if (valueClass.equals(TypeManager.getWrapperClass(targetClass)))
            return value;


        return null;
    }
}
