package com.zhaofujun.automapper.mapping;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.zhaofujun.automapper.core.FieldInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class CommonFieldInfo extends FieldInfo {

    private Logger logger = LoggerFactory.getLogger(CommonFieldInfo.class);

    public CommonFieldInfo(String mappingField, Field field) {
        super(mappingField, field);
    }

    @Override
    public Object getFieldValue(Object object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            logger.warn("获取字段"+field.getName()+"失败");
            return null;
        }
    }

    @Override
    public void setFieldValue(Object object, Object value) {
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            logger.warn("写入字段"+field.getName()+"失败");
        }
    }


}
