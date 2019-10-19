package com.zhaofujun.automapper;

import com.zhaofujun.automapper.builder.ClassMappingBuilder;
import com.zhaofujun.automapper.builder.DefaultClassMappingBuilder;
import com.zhaofujun.automapper.mapping.ClassMappingManager;
import com.zhaofujun.automapper.mapping.FieldMapping;

import java.util.List;

public class AutoMapper implements IMapper {

    private ClassMappingManager classMappingManager = new ClassMappingManager();

    @Override
    public void map(Object source, Object target) {
    }

    @Override
    public <T> T map(Object source, Class<T> targetClass) {
        List<FieldMapping> fieldMappingList = classMappingManager.getFieldMappingList(source.getClass(), targetClass);
        return null;
    }

    @Override
    public ClassMappingBuilder mapping(Class sourceClass,Class targetClass) {
        return mapping(sourceClass,targetClass, false);
    }

    @Override
    public ClassMappingBuilder mapping(Class sourceClass,Class targetClass, boolean allowPrivate) {

        ClassMappingBuilder classBuilder = new DefaultClassMappingBuilder(sourceClass, targetClass, allowPrivate);

        classMappingManager.addClassMapping(classBuilder.builder());

        return classBuilder;
    }
}

