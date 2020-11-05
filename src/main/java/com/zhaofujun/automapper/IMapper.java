package com.zhaofujun.automapper;


import com.zhaofujun.automapper.builder.ClassMappingBuilder;
import com.zhaofujun.automapper.converter.Converter;

public interface IMapper {
    default void map(Object source, Object target, String... excludesTargetFieldNames) {
        map(source, target, false, excludesTargetFieldNames);
    }

    default <T> T map(Object source, Class<T> targetClass, String... excludesTargetFieldNames) {
        return map(source, targetClass, false, excludesTargetFieldNames);
    }

    void map(Object source, Object target, boolean allowNoSetter, String... excludesTargetFieldNames);

    default  <T> T map(Object source, Class<T> targetClass, boolean allowNoSetter, String... excludesTargetFieldNames){
        try {
            T target = targetClass.getConstructor().newInstance();
            map(source, target, allowNoSetter, excludesTargetFieldNames);
            return target;
        } catch (Exception ex) {
            return null;
        }
    }

    IMapper registerConverter(Converter converter);

    //自动匹配所有带set的字段
    ClassMappingBuilder mapping(Class sourceClass, Class targetClass);
}

