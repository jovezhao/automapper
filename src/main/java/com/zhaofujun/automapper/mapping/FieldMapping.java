package com.zhaofujun.automapper.mapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FieldMapping {
    enum MappingState {
        YES, NO
    }

    private Logger logger = LoggerFactory.getLogger(FieldMapping.class);


    private FieldInfo sourceField; //源字段
    private FieldInfo targetField; //目标字段
    private MappingState mappingState = MappingState.YES; //映射状态

    //同名字段的构建,如果没有setter，默认不映射
    public FieldMapping(FieldInfo targetField, Class sourceClass) {
        this.targetField = targetField;

        try {
            this.sourceField = FieldInfo.create(sourceClass, targetField.getField().getName());
        } catch (NotFoundFieldException ex) {
            logger.info("没有找到同名字段");
        }
    }


    //排除当前映射
    public void exclude() {
        this.mappingState = MappingState.NO;
    }


    //指定映射字段
    public void map(FieldInfo sourceField) {
        this.sourceField = sourceField;
        this.mappingState = MappingState.YES;

    }

    public FieldInfo getSourceField() {
        return sourceField;
    }

    public FieldInfo getTargetField() {
        return targetField;
    }

    public MappingState getMappingState() {
        return mappingState;
    }


}
