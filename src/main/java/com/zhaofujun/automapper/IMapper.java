package com.zhaofujun.automapper;


import com.zhaofujun.automapper.builder.ClassMappingBuilder;
import com.zhaofujun.automapper.map.Converter;

public interface IMapper {
    void map(Object source, Object target);

    <T> T map(Object source, Class<T> targetClass);

    IMapper registerConverter(Converter converter);

    //自动匹配所有带set的字段
    ClassMappingBuilder mapping(Class sourceClass,Class targetClass);
    //自动匹配所有字段
    ClassMappingBuilder mapping(Class sourceClass,Class targetClass,boolean allowPrivate);
}

