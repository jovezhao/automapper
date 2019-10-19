package com.zhaofujun.automapper;


import com.zhaofujun.automapper.builder.ClassMappingBuilder;

public interface IMapper {
    void map(Object source, Object target);

    <T> T map(Object source, Class<T> targetClass);

    //自动匹配所有带set的字段
    ClassMappingBuilder mapping(Class sourceClass,Class targetClass);
    //自动匹配所有字段
    ClassMappingBuilder mapping(Class sourceClass,Class targetClass,boolean allowPrivate);
}

