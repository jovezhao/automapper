package com.zhaofujun.automapper.builder;

import com.zhaofujun.automapper.mapping.ClassMapping;
import com.zhaofujun.automapper.mapping.FieldMapping;
import com.zhaofujun.automapper.reflect.BeanUtils;

import java.lang.reflect.Field;

public class DefaultClassMappingBuilder implements ClassMappingBuilder {

    private ClassMapping classMapping;

    public DefaultClassMappingBuilder(Class sourceClass, Class targetClass, boolean allowPrivate) {
        this.classMapping = new ClassMapping(sourceClass, targetClass, allowPrivate);
    }

    @Override
    public ClassMappingBuilder field(String sourceFieldName,String targetFieldName) {
        Field targetField = BeanUtils.getField(classMapping.getTargetClass(), targetFieldName);
        FieldMapping targetFieldMapping = classMapping.getFieldMapping(targetField);

        Field sourceField = BeanUtils.getField(classMapping.getSourceClass(), sourceFieldName);
        targetFieldMapping.map(sourceField);

        return this;
    }

    @Override
    public ClassMappingBuilder excludes(String[] targetFieldNames) {
        for (String targetFieldName : targetFieldNames) {
            Field targetField = BeanUtils.getField(classMapping.getTargetClass(), targetFieldName);
            FieldMapping targetFieldMapping = classMapping.getFieldMapping(targetField);
            targetFieldMapping.exclude();
        }
        return this;
    }

    @Override
    public ClassMapping builder() {
        return classMapping;
    }
}