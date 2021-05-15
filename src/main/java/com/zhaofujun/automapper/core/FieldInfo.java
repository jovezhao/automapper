package com.zhaofujun.automapper.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public abstract class FieldInfo {

    protected String mappingField;
    protected Field field;
    protected FieldInfo next;


    private Logger logger = LoggerFactory.getLogger(FieldInfo.class);


    public Class getNextType() {
        if (next == null) return field.getType();
        return next.getNextType();
    }

    public FieldInfo(String mappingField, Field field ) {
        this.mappingField = mappingField;
        this.field = field;
    }

    public void setNext(FieldInfo next) {
        this.next = next;
    }

    public Field getField() {
        return field;
    }

    public String getMappingField() {
        return mappingField;
    }

    public FieldInfo getNext() {
        return next;
    }

    public abstract Object getFieldValue(Object object);

    public abstract void setFieldValue(Object object, Object value);

    public Object getValue(Object object) {
        Object fieldValue = getFieldValue(object);

        if (next == null)
            return fieldValue;
        else
            return next.getFieldValue(fieldValue);
    }

    public void setValue(Object object, Object value) {
        if (this.next == null) {
            setFieldValue(object, value);
            return;
        } else {
            //如果有子字段，并且当前字段值为空，先使用默认构造函数生成一个
            Object fieldValue = getFieldValue(object);
            try {
                if (fieldValue == null) {
                    fieldValue = field.getType().getConstructor().newInstance();
                    this.setFieldValue(object, fieldValue);
                }
                this.next.setValue(fieldValue, value);
            }catch (Exception exception){
                logger.warn("写入子对象失败");
            }
        }
    }


}
