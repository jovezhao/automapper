package com.zhaofujun.automapper.mapping;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.zhaofujun.automapper.core.FieldInfo;
import com.zhaofujun.automapper.utils.BeanUtils;

import java.lang.reflect.Field;

public class FieldInfoFactory {
    public static FieldInfo create(Field field ) {
        FieldInfo fieldInfo = new CommonFieldInfo(field.getName(), field);
        return fieldInfo;
    }

    public static FieldInfo create(Class clazz, String mappingField ) throws NotFoundFieldException {
        //识别mappingField，根据语法生成FieldInfo
        //1. 以.来区分下级对象
        //2. 普通字段 xxx_
        //3. 列表字段[index]
        //4. map字段["key"]
        int index = mappingField.indexOf(".");
        String fieldName = mappingField;
        String nextFieldName = null;
        if (index != -1) {
            fieldName = mappingField.substring(0, index);

            nextFieldName = mappingField.substring(index + 1);
        }
        Field field = BeanUtils.getField(clazz, fieldName); //查询field

        FieldInfo fieldInfo = null;
        if (true) //todo 根据语法创建具体的实体
            fieldInfo = new CommonFieldInfo(mappingField, field);

        if (nextFieldName != null)
            fieldInfo.setNext(create(clazz, nextFieldName));

        return fieldInfo;
    }
}
