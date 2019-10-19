package com.zhaofujun.automapper.mapping;

import com.zhaofujun.automapper.reflect.BeanUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ClassMapping {


    private Class sourceClass;
    private Class targetClass;
    private boolean allowNoSetter;
    private Map<Field, FieldMapping> fieldMappingItemMap;

    public ClassMapping(Class sourceClass, Class targetClass) {
        this(sourceClass, targetClass, false);
    }

    public ClassMapping(Class sourceClass, Class targetClass, boolean allowNoSetter) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        this.allowNoSetter = allowNoSetter;
        fieldMappingItemMap = new HashMap<>();

        for (Field field : BeanUtils.getAllFields(targetClass)) {
            fieldMappingItemMap.put(field, new FieldMapping(field, sourceClass));
        }

    }


    public Class getSourceClass() {
        return sourceClass;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public boolean isAllowNoSetter() {
        return allowNoSetter;
    }

    // 获取有效的映射列表
    public List<FieldMapping> getEffectiveFieldMappingList() {
        Stream<FieldMapping> fieldMappingStream = fieldMappingItemMap.values()
                .stream()
                .filter(p -> p.getMappingState() == FieldMapping.MappingState.YES)
                .filter(p -> p.getSourceField() != null);

        if (!allowNoSetter) {
            fieldMappingStream = fieldMappingStream
                    .filter(p -> p.getTargetSetter() != null && p.getTargetSetter() != null);
        }
        return fieldMappingStream.collect(Collectors.toList());

    }

    public FieldMapping getFieldMapping(Field targetField){
       return fieldMappingItemMap.get(targetField);
    }
}

