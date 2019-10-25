package com.zhaofujun.automapper.mapping;

import com.zhaofujun.automapper.reflect.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FieldInfo {

    private Logger logger = LoggerFactory.getLogger(FieldInfo.class);

    public static FieldInfo create(Class clazz, String mappingField) throws NotFoundFieldException {

        String fieldName;
        int i = mappingField.indexOf(".");
        if (i == -1) {
            fieldName = mappingField;
        } else {
            fieldName = mappingField.substring(0, i);
        }
        Field field = BeanUtils.getField(clazz, fieldName);
        if (field == null)
            throw new NotFoundFieldException("在类型" + clazz.getName() + "下没有找到字段为" + fieldName + "的字段");

        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.field = field;
        fieldInfo.mappingField = mappingField;
        fieldInfo.getterMethod = BeanUtils.getGetter(field);
        fieldInfo.setterMethod = BeanUtils.getSetter(field);

        if (i != -1) {
            fieldInfo.next = create(field.getType(), mappingField.substring(i + 1, mappingField.length()));
        }
        return fieldInfo;
    }

    public Class getNextType() {
        if (next == null) return field.getType();
        return next.getNextType();
    }

    private FieldInfo() {

    }

    private String mappingField;
    private Field field;
    private Method getterMethod;
    private Method setterMethod;

    public Field getField() {
        return field;
    }

    public Method getGetterMethod() {
        return getterMethod;
    }

    public Method getSetterMethod() {
        return setterMethod;
    }

    private FieldInfo next;

    public String getMappingField() {
        return mappingField;
    }

    public FieldInfo getNext() {
        return next;
    }

    public Object getValue(Object object) {
        Object value = this.getFieldValue(object);

        if (this.next != null && value != null) {
            return next.getValue(value);
        }
        return value;
    }

    public void setValue(Object object, Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (this.next == null) {
            setFieldValue(object, value);
            return;
        }
        //如果有子字段，并且当前字段值为空，先使用默认构造函数生成一个
        Object fieldValue = getFieldValue(object);
        if (fieldValue == null) {
            fieldValue = field.getType().getConstructor().newInstance();
            this.setFieldValue(object, fieldValue);
        }
        this.next.setValue(fieldValue, value);

    }


    private void setFieldValue(Object object, Object value) {
        try {
            if (this.getSetterMethod() != null) {
                this.getSetterMethod().invoke(object, value);
            } else {
                this.getField().setAccessible(true);
                this.getField().set(object, value);
            }
        } catch (Exception ex) {
            logger.info("赋值时发生异常");
            ex.printStackTrace();
        }
    }

    private Object getFieldValue(Object object) {
        try {
            if (this.getGetterMethod() != null) {
                return this.getGetterMethod().invoke(object);
            } else {
                this.getField().setAccessible(true);
                return this.getField().get(object);
            }
        } catch (Exception ex) {
            return null;
        }
    }
}
