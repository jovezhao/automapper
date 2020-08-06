package com.zhaofujun.automapper.mapping;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.zhaofujun.automapper.converter.Converter;
import com.zhaofujun.automapper.converter.ConverterInfo;
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
    private ConverterInfo converterInfo;

    //同名字段的构建,如果没有setter，默认不映射
    public FieldMapping(FieldInfo targetField, Class sourceClass,MethodAccess sourceMethodAccess) {
        this.targetField = targetField;

        try {
            this.sourceField = FieldInfo.create(sourceClass, targetField.getField().getName(),sourceMethodAccess);
        } catch (NotFoundFieldException ex) {
            logger.info("在源类型{}中没有找到同名字段{}", sourceClass.getName(), targetField.getField().getName());
        }
    }


    //排除当前映射
    public void exclude() {
        this.mappingState = MappingState.NO;
    }


    //指定映射字段
    public void map(FieldInfo sourceField, Converter converter) {
        this.sourceField = sourceField;
        this.mappingState = MappingState.YES;

        if (converter == null)
            return;
        if (targetField.getField().getType().isAssignableFrom(converter.getTargetClass()) &&sourceField.getField().getType().isAssignableFrom(converter.getSourceClass())) {
            converterInfo = new ConverterInfo(converter, ConverterInfo.Direction.positive);
        }
        if (sourceField.getField().getType().isAssignableFrom(converter.getTargetClass()) && targetField.getField().getType().isAssignableFrom(converter.getSourceClass())) {
            converterInfo = new ConverterInfo(converter, ConverterInfo.Direction.negative);
        }
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

    public ConverterInfo getConverterInfo() {
        return converterInfo;
    }
}
