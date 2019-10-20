package com.zhaofujun.automapper.map;

import java.util.ArrayList;
import java.util.List;

public class TypeManager {
    private static List<BaseAndWrapper> baseAndWrappers = new ArrayList<>();

    static {
        baseAndWrappers.add(new BaseAndWrapper(int.class, Integer.class));
        baseAndWrappers.add(new BaseAndWrapper(boolean.class, Boolean.class));
        baseAndWrappers.add(new BaseAndWrapper(long.class, Long.class));
        baseAndWrappers.add(new BaseAndWrapper(float.class, Float.class));
        baseAndWrappers.add(new BaseAndWrapper(short.class, Short.class));
        baseAndWrappers.add(new BaseAndWrapper(double.class, Double.class));
        baseAndWrappers.add(new BaseAndWrapper(byte.class, Byte.class));
        baseAndWrappers.add(new BaseAndWrapper(char.class, Character.class));
    }


    public static boolean isWrapper(Class clazz) {
        return baseAndWrappers.stream()
                .filter(p -> p.getWrapperClass().equals(clazz))
                .count() > 0;
    }

    public static boolean isBase(Class clazz) {
        return baseAndWrappers.stream()
                .filter(p -> p.getBaseClass().equals(clazz))
                .count() > 0;
    }

    public static Class getWrapperClass(Class clazz) {
        return baseAndWrappers.stream()
                .filter(p -> p.getBaseClass().equals(clazz))
                .map(p -> p.getWrapperClass())
                .findFirst()
                .orElse(null);
    }
}
