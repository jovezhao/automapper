package com.zhaofujun.automapper.builder;

import com.zhaofujun.automapper.mapping.ClassMapping;
import com.zhaofujun.automapper.mapping.FieldInfo;
import com.zhaofujun.automapper.mapping.FieldMapping;
import com.zhaofujun.automapper.mapping.NotFoundFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultClassMappingBuilder implements ClassMappingBuilder {

    private Logger logger = LoggerFactory.getLogger(DefaultClassMappingBuilder.class);

    private ClassMapping classMapping;

    public DefaultClassMappingBuilder(Class sourceClass, Class targetClass, boolean allowPrivate) {
        this.classMapping = new ClassMapping(sourceClass, targetClass, allowPrivate);
    }

    @Override
    public ClassMappingBuilder field(String sourceFieldName, String targetFieldName) {

        try {
            FieldMapping targetFieldMapping = classMapping.getFieldMapping(targetFieldName);
            if (targetFieldMapping == null) {
                //如果没有在目标对象中找到对应的字段，有可能目标是复合属性，可以直接添加一个新的字段映射信息
                targetFieldMapping = classMapping.createFieldMapping(targetFieldName);
            }

            FieldInfo sourceField = FieldInfo.create(classMapping.getSourceClass(), sourceFieldName);
            targetFieldMapping.map(sourceField);
        } catch (NotFoundFieldException ex) {
            logger.info("没有找到字段", ex);
        }
        return this;
    }

    @Override
    public ClassMappingBuilder excludes(String... targetFieldNames) {
        for (String targetFieldName : targetFieldNames) {
            FieldMapping targetFieldMapping = classMapping.getFieldMapping(targetFieldName);
            targetFieldMapping.exclude();
        }
        return this;
    }

    @Override
    public ClassMapping getClassMapping() {
        return classMapping;
    }
}