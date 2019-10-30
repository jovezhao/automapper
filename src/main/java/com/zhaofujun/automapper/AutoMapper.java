package com.zhaofujun.automapper;

import com.zhaofujun.automapper.builder.ClassMappingBuilder;
import com.zhaofujun.automapper.builder.DefaultClassMappingBuilder;
import com.zhaofujun.automapper.map.Converter;
import com.zhaofujun.automapper.map.ConverterInfo;
import com.zhaofujun.automapper.map.ConverterManager;
import com.zhaofujun.automapper.map.TypeManager;
import com.zhaofujun.automapper.mapping.ClassMappingManager;
import com.zhaofujun.automapper.mapping.FieldMapping;

import java.util.List;

public class AutoMapper implements IMapper {

    private ClassMappingManager classMappingManager = new ClassMappingManager();
    private ConverterManager converterManager = new ConverterManager();

    @Override
    public void map(Object source, Object target) {
        List<FieldMapping> fieldMappingList = classMappingManager.getFieldMappingList(source.getClass(), target.getClass());
        fieldMappingList.forEach(p -> {
            try { //
                Object value = p.getSourceField().getValue(source);

                Object newValue = parseValue(value, p.getSourceField().getNextType(), p.getTargetField().getNextType());
                p.getTargetField().setValue(target, newValue);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }


    @Override
    public <T> T map(Object source, Class<T> targetClass) {
        try {
            T target = targetClass.getConstructor().newInstance();
            map(source, target);
            return target;
        } catch (Exception ex) {
            return null;
        }
    }


    @Override
    public ClassMappingBuilder mapping(Class sourceClass, Class targetClass) {
        return mapping(sourceClass, targetClass, false);
    }

    @Override
    public ClassMappingBuilder mapping(Class sourceClass, Class targetClass, boolean allowPrivate) {

        ClassMappingBuilder classBuilder = new DefaultClassMappingBuilder(sourceClass, targetClass, allowPrivate);

        classMappingManager.addClassMapping(classBuilder.getClassMapping());

        return classBuilder;
    }

    private Object parseValue(Object value, Class valueClass, Class targetClass) throws Exception {
        // 先判断两个类型是否一致，如果一致，直接使用
        if (valueClass.equals(targetClass))
            return value;

        // 查看自定义转换器是否有匹配，如果有使用转换器转换
        ConverterInfo converterInfo = converterManager.getConverter(valueClass, targetClass);
        if (converterInfo != null) {
            if (converterInfo.getDirection() == ConverterInfo.Direction.negative)
                return converterInfo.getConverter().toSource(value);
            return converterInfo.getConverter().toTarget(value);
        }


        // 如果目标类型是源类型的包装器,通过包装器类型的valueOf静态方法创建对象
        if (targetClass.equals(TypeManager.getWrapperClass(valueClass)))
            return targetClass.getDeclaredMethod("valueOf", valueClass).invoke(null, value);

        // 如果源类类型是包装器，目标类型是基础类型，可以直接返回
        if (valueClass.equals(TypeManager.getWrapperClass(targetClass)))
            return value;


        // 如果目标是字符串，直接使用toString返回
        if (targetClass.equals(String.class))
            return value == null ? null : value.toString();

        //如果目标类型是包装器，将值转换为字符串后用包装器valueOf的字符串方式创建对象
        if (TypeManager.isWrapper(targetClass))
            return targetClass.getDeclaredMethod("valueOf", String.class).invoke(null, value.toString());

        //如果目标是基础类型，将值转换为字符串后用包装器的value方法转换成基础类型
        if (TypeManager.isBase(targetClass)) {
            Class wrapperClass = TypeManager.getWrapperClass(targetClass);
            Object wrapperObject = wrapperClass.getDeclaredMethod("valueOf", String.class).invoke(null, value.toString());
            return wrapperClass.getDeclaredMethod(targetClass.getName() + "Value").invoke(wrapperObject);
        }

        //其于的都使用对象转换工具直接转换
        return map(value, targetClass);

    }

    public IMapper registerConverter(Converter converter) {
        converterManager.addConverter(converter);
        return this;
    }
}

