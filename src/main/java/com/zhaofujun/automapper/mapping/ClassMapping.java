package com.zhaofujun.automapper.mapping;

import com.esotericsoftware.reflectasm.MethodAccess;
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
    private boolean allowNoSetter;
    private Map<String, FieldMapping> fieldMappingItemMap;
    private MethodAccess sourceMethodAccess;
    private MethodAccess targetMethodAccess;


    public ClassMapping(Class sourceClass, Class targetClass) {
        this(sourceClass, targetClass, false);
    }

    public ClassMapping(Class sourceClass, Class targetClass, boolean allowNoSetter) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        this.allowNoSetter = allowNoSetter;
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
}

