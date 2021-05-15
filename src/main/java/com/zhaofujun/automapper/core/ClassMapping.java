package com.zhaofujun.automapper.core;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.zhaofujun.automapper.mapping.FieldInfoFactory;
import com.zhaofujun.automapper.mapping.NotFoundFieldException;
import com.zhaofujun.automapper.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class ClassMapping {
    private Logger logger = LoggerFactory.getLogger(ClassMapping.class);
    private Class sourceClass;
    private Class targetClass;
    private Map<String, FieldMapping> fieldMappingItemMap;
    private MethodAccess sourceMethodAccess;
    private MethodAccess targetMethodAccess;


    public ClassMapping(Class sourceClass, Class targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        fieldMappingItemMap = new HashMap<>();
        this.targetMethodAccess = MethodAccess.get(targetClass);
        this.sourceMethodAccess = MethodAccess.get(sourceClass);

    }

    private FieldMapping loadFieldMapping(String targetFieldName) throws NotFoundFieldException {
        FieldMapping fieldMapping = fieldMappingItemMap.get(targetFieldName);
        if (fieldMapping == null) {
            FieldInfo targetFieldInfo = FieldInfoFactory.create(targetClass, targetFieldName);
            fieldMapping = new FieldMapping(targetFieldInfo,this);
            fieldMappingItemMap.put(targetFieldName, fieldMapping);
        }
        return fieldMapping;
    }

    public ClassMapping field(String sourceFieldName, String targetFieldName) {
        return field(sourceFieldName, targetFieldName, null);
    }

    public ClassMapping field(String sourceFieldName, String targetFieldName, Converter converter) {
        try {
            FieldMapping fieldMapping = loadFieldMapping(targetFieldName);
            FieldInfo sourceFieldInfo = FieldInfoFactory.create(sourceClass, sourceFieldName);
            fieldMapping.mapping(sourceFieldInfo, converter);
        } catch (NotFoundFieldException exception) {
            logger.warn("没有找到字段" + targetFieldName, exception);
        }
        return this;
    }

    public ClassMapping exclude(String... targetFieldNames) {
        for (String targetFieldName : targetFieldNames) {
            try {
                FieldMapping fieldMapping = loadFieldMapping(targetFieldName);
                fieldMapping.exclude();
            } catch (NotFoundFieldException exception) {
                logger.warn("没有找到字段" + targetFieldName, exception);
            }
        }
        return this;
    }


    public Map<String, FieldMapping> getConfigurationFieldMapping() {
        return fieldMappingItemMap;
    }

    public Map<String, FieldMapping> getAllFieldMapping() {
        Map<String, FieldMapping> fieldMappings = new HashMap<>();
        for (Field field : BeanUtils.getAllFields(targetClass)) {
            try {
                FieldInfo targetField = FieldInfoFactory.create(field);
                FieldInfo sourceField = FieldInfoFactory.create(sourceClass, field.getName());
                FieldMapping fieldMapping = new FieldMapping(targetField,this);
                fieldMapping.mapping(sourceField);
                fieldMappings.put(field.getName(), fieldMapping);
            } catch (NotFoundFieldException ex) {
                logger.debug(ex.getMessage());
            }
        }
        return fieldMappings;
    }

    public ClassMapping reverse() {
        ClassMapping classMapping = new ClassMapping(targetClass, sourceClass);

        this.fieldMappingItemMap.values().forEach(p -> {
            classMapping.field(p.getTargetField().getMappingField(), p.getSourceField().getMappingField(), p.getConverter());
            if (p.getState() == MappingState.NO)
                classMapping.exclude(p.getSourceField().getMappingField());
        });
        return classMapping;
    }


    public Class getSourceClass() {
        return sourceClass;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public MethodAccess getSourceMethodAccess() {
        return sourceMethodAccess;
    }

    public MethodAccess getTargetMethodAccess() {
        return targetMethodAccess;
    }


}

