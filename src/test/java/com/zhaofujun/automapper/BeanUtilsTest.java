package com.zhaofujun.automapper;

import com.zhaofujun.automapper.reflect.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeanUtilsTest {
    public static void main(String[] args) {
        ClassA classA = new ClassA();
        Field[] fields = BeanUtils.getAllFields(ClassA.class);

        Method setter = BeanUtils.getSetter(fields[0]);
        Method getter = BeanUtils.getGetter(fields[0]);
        Method getter1 = BeanUtils.getGetter(fields[1]);
        Method getter2 = BeanUtils.getGetter(fields[2]);
    }
}
