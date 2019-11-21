package com.zhaofujun.automapper.builder;

import com.zhaofujun.automapper.converter.Converter;
import com.zhaofujun.automapper.mapping.ClassMapping;
import com.zhaofujun.automapper.mapping.FieldInfo;
import com.zhaofujun.automapper.mapping.FieldMapping;
import com.zhaofujun.automapper.mapping.NotFoundFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClassMappingBuilder   {

    private Logger logger = LoggerFactory.getLogger(ClassMappingBuilder.class);

    private ClassMapping classMapping;

    public ClassMappingBuilder(Class sourceClass, Class targetClass, boolean allowPrivate) {
        this.classMapping = new ClassMapping(sourceClass, targetClass, allowPrivate);
    }


    public ClassMappingBuilder field(String sourceFieldName, String targetFieldName) {
        return field(sourceFieldName, targetFieldName, null);
    }


    public ClassMappingBuilder field(String sourceFieldName, String targetFieldName, Converter converter) {

        try {
            FieldMapping targetFieldMapping = classMapping.getFieldMapping(targetFieldName);
            if (targetFieldMapping == null) {
                //如果没有在目标对象中找到对应的字段，有可能目标是复合属性，可以直接添加一个新的字段映射信息
                targetFieldMapping = classMapping.createFieldMapping(targetFieldName);
            }

            FieldInfo sourceField = FieldInfo.create(classMapping.getSourceClass(), sourceFieldName);
            targetFieldMapping.map(sourceField, converter);
        } catch (NotFoundFieldException ex) {
            logger.info(ex.getMessage());
        }
        return this;
    }


    public ClassMappingBuilder excludes(String... targetFieldNames) {
        for (String targetFieldName : targetFieldNames) {
            FieldMapping targetFieldMapping = classMapping.getFieldMapping(targetFieldName);
            targetFieldMapping.exclude();
        }
        return this;
    }


    public ClassMapping builder() {
        return classMapping;
    }
}