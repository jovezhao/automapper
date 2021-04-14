package com.zhaofujun.automapper;

import com.zhaofujun.automapper.converter.Converter;
import com.zhaofujun.automapper.converter.ConverterInfo;
import com.zhaofujun.automapper.converter.ConverterManager;
import com.zhaofujun.automapper.mapping.ClassMapping;
import com.zhaofujun.automapper.mapping.ClassMappingManager;
import com.zhaofujun.automapper.mapping.FieldMapping;
import com.zhaofujun.automapper.mapping.ParseValueException;
import com.zhaofujun.automapper.utils.TypeUtiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class AutoMapper implements IMapper {
    private Logger logger = LoggerFactory.getLogger(AutoMapper.class);

    private ClassMappingManager classMappingManager = new ClassMappingManager();
    private ConverterManager converterManager = new ConverterManager();

    @Override
    public void map(Object source, Object target, boolean isAccessible, String... excludesTargetFieldNames) {

        List<FieldMapping> fieldMappingList = classMappingManager.getFieldMappingList(source.getClass(), target.getClass(), isAccessible);

        fieldMappingList.stream()
                .filter(p -> !Arrays.asList(excludesTargetFieldNames).contains(p.getTargetField().getMappingField()))
                .forEach(p -> {
                    try { //
                        Object value = p.getSourceField().getValue(source);

                        Object newValue;
                        if (p.getConverterInfo() != null)
                            newValue = p.getConverterInfo().convert(value);
                        else
                            newValue = parseValue(value, p.getSourceField().getNextType(), p.getTargetField().getNextType(), isAccessible);

                        p.getTargetField().setValue(target, newValue);

                    } catch (ParseValueException ex) {
                        logger.debug("映射{0}类型{1}字段失败,失败原因：{2}", target.getClass().getName(), p.getTargetField().getField().getName(), ex.getMessage());
                    } catch (Exception ex) {
                        logger.debug("映射{0}类型{1}字段失败,失败原因：{2}", target.getClass().getName(), p.getTargetField().getField().getName(), ex.getMessage());
                    }
                });

    }

    @Override
    public ClassMapping mapping(Class sourceClass, Class targetClass) {
        return classMappingManager.getClassMapping(sourceClass, targetClass);
    }


    private Object parseValue(Object value, Class valueClass, Class targetClass, boolean isAccessible) throws Exception {
        if (value == null) return null;
        // 先判断两个类型是否一致，如果一致，直接使用
        if (valueClass.equals(targetClass))
            return value;

        // 查看自定义转换器是否有匹配，如果有使用转换器转换
        ConverterInfo converterInfo = converterManager.getConverter(valueClass, targetClass);
        if (converterInfo != null)
            return converterInfo.convert(value);


        // 如果目标类型是源类型的包装器,通过包装器类型的valueOf静态方法创建对象
        if (targetClass.equals(TypeUtiles.getWrapperClass(valueClass)))
            return targetClass.getDeclaredMethod("valueOf", valueClass).invoke(null, value);

        // 如果源类类型是包装器，目标类型是基础类型，可以直接返回
        if (valueClass.equals(TypeUtiles.getWrapperClass(targetClass)))
            return value;


        // 如果目标是字符串，直接使用toString返回
        if (targetClass.equals(String.class))
            return value == null ? null : value.toString();

        //如果目标类型是包装器，将值转换为字符串后用包装器valueOf的字符串方式创建对象
        if (targetClass.isEnum() && value != null)
            return targetClass.getDeclaredMethod("valueOf", String.class).invoke(null, value.toString());

        //如果目标类型是包装器，将值转换为字符串后用包装器valueOf的字符串方式创建对象
        if (TypeUtiles.isWrapper(targetClass) && value != null)
            return targetClass.getDeclaredMethod("valueOf", String.class).invoke(null, value.toString());

        //如果目标类型是BigInteger或BigDecimal，将值转换为字符串后使用构造函数创建
        if (targetClass.equals(BigDecimal.class))
            return value == null ? null : new BigDecimal(value.toString());
        if (targetClass.equals(BigInteger.class))
            return value == null ? null : new BigInteger(value.toString());


        //如果目标是基础类型，将值转换为字符串后用包装器的value方法转换成基础类型
        if (TypeUtiles.isBase(targetClass)) {
            Class wrapperClass = TypeUtiles.getWrapperClass(targetClass);
            Object wrapperObject = wrapperClass.getDeclaredMethod("valueOf", String.class).invoke(null, value.toString());
            return wrapperClass.getDeclaredMethod(targetClass.getName() + "Value").invoke(wrapperObject);
        }

        //其于的都使用对象转换工具直接转换
        Object object = map(value, targetClass, isAccessible);
        if (value != null && object == null)
            throw new ParseValueException("源类型：" + valueClass.getName() + "转换到目标类型：" + targetClass.getName() + "失败");
        return value;

    }

    public IMapper registerConverter(Converter converter) {
        converterManager.addConverter(converter);
        return this;
    }

    public ClassMappingManager getClassMappingManager() {
        return classMappingManager;
    }

    public ConverterManager getConverterManager() {
        return converterManager;
    }
}

