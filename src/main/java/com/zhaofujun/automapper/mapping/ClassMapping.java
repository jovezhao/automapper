package com.zhaofujun.automapper.mapping;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.zhaofujun.automapper.converter.Converter;
import com.zhaofujun.automapper.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ClassMapping {

    private Logger logger = LoggerFactory.getLogger(ClassMapping.class);
    private Class sourceClass;
    private Class targetClass;
    private Map<String, FieldMapping> fieldMappingItemMap;
    private MethodAccess sourceMethodAccess;
    private MethodAccess targetMethodAccess;


    public ClassMapping(Class sourceClass, Class targetClass  ) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        fieldMappingItemMap = new HashMap<>();
        this.targetMethodAccess = MethodAccess.get(targetClass);
        this.sourceMethodAccess = MethodAccess.get(sourceClass);

        for (Field field : BeanUtils.getAllFields(targetClass)) {
            try {
                FieldInfo targetField = FieldInfo.create(targetClass, field.getName(), targetMethodAccess);
                fieldMappingItemMap.put(field.getName(), new FieldMapping(targetField, sourceClass, sourceMethodAccess));
            } catch (NotFoundFieldException ex) {
                logger.debug(ex.getMessage());
            }
        }
    }


    public Class getSourceClass() {
        return sourceClass;
    }

    public Class getTargetClass() {
        return targetClass;
    }


    // 获取有效的映射列表
    public List<FieldMapping> getEffectiveFieldMappingList(boolean allowNoSetter) {
        Stream<FieldMapping> fieldMappingStream = fieldMappingItemMap.values()
                .stream()
                .filter(p -> p.getMappingState() == FieldMapping.MappingState.YES)
                .filter(p -> p.getSourceField() != null);

        if (!allowNoSetter) {
            fieldMappingStream = fieldMappingStream
                    .filter(p -> p.getTargetField().getSetterMethod() != null && p.getSourceField().getGetterMethod() != null);
        }
        return fieldMappingStream.collect(Collectors.toList());

    }

    public FieldMapping getFieldMapping(String targetFieldName) {
        return fieldMappingItemMap.get(targetFieldName);
    }

    public FieldMapping createFieldMapping(String targetFieldName) throws NotFoundFieldException {
        FieldInfo targetField = FieldInfo.create(targetClass, targetFieldName, targetMethodAccess);
        FieldMapping fieldMapping = new FieldMapping(targetField, sourceClass, sourceMethodAccess);
        fieldMappingItemMap.put(targetFieldName, fieldMapping);
        return fieldMapping;
    }

    public MethodAccess getSourceMethodAccess() {
        return sourceMethodAccess;
    }

    public MethodAccess getTargetMethodAccess() {
        return targetMethodAccess;
    }

    public ClassMapping field(String sourceFieldName, String targetFieldName) {
        return field(sourceFieldName, targetFieldName, null);
    }
    public ClassMapping field(String sourceFieldName, String targetFieldName, Converter converter) {

        try {
            FieldMapping targetFieldMapping = this.getFieldMapping(targetFieldName);
            if (targetFieldMapping == null) {
                //如果没有在目标对象中找到对应的字段，有可能目标是复合属性，可以直接添加一个新的字段映射信息
                targetFieldMapping = this.createFieldMapping(targetFieldName);
            }

            FieldInfo sourceField = FieldInfo.create(this.getSourceClass(), sourceFieldName, this.getSourceMethodAccess());
            targetFieldMapping.map(sourceField, converter);
        } catch (NotFoundFieldException ex) {
            logger.debug(ex.getMessage());
        }
        return this;
    }

    public ClassMapping excludes(String... targetFieldNames) {
        for (String targetFieldName : targetFieldNames) {
            FieldMapping targetFieldMapping = this.getFieldMapping(targetFieldName);
            targetFieldMapping.exclude();
        }
        return this;
    }}

