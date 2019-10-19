package com.zhaofujun.automapper.mapping;

import com.zhaofujun.automapper.reflect.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FieldMapping {
    enum MappingState {
        YES, NO
    }

    private Field sourceField; //源字段
    private Method sourceGetter; //源字段对应的getter方法
    private Field targetField; //目标字段
    private Method targetSetter; //目标字段对应的setter，可为空
    private MappingState mappingState=MappingState.YES; //映射状态

    //同名字段的构建,如果没有setter，默认不映射
    public FieldMapping(Field targetField, Class sourceClass) {
        this.targetField = targetField;
        this.targetSetter = BeanUtils.getSetter(targetField);

        Field sourceField = BeanUtils.getField(sourceClass, targetField.getName());
        if (sourceField != null) {
            this.sourceField = sourceField;
            this.sourceGetter = BeanUtils.getGetter(sourceField);
        }
    }


    //排除当前映射
    public void exclude() {
        this.mappingState = MappingState.NO;
    }


    //指定映射字段
    public void map(Field sourceField) {
        this.sourceField = sourceField;
        this.sourceGetter = BeanUtils.getGetter(sourceField);
        this.mappingState = MappingState.YES;

    }


    public Field getSourceField() {
        return sourceField;
    }

    public Method getSourceGetter() {
        return sourceGetter;
    }

    public Field getTargetField() {
        return targetField;
    }

    public Method getTargetSetter() {
        return targetSetter;
    }

    public MappingState getMappingState() {
        return mappingState;
    }
}
