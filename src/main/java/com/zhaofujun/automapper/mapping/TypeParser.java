package com.zhaofujun.automapper.mapping;

import com.zhaofujun.automapper.core.Converter;
import com.zhaofujun.automapper.core.ConverterVerifyState;
import com.zhaofujun.automapper.core.IMapper;
import com.zhaofujun.automapper.core.IMapping;
import com.zhaofujun.automapper.utils.TypeUtiles;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TypeParser {
    private IMapper mapper;
    private IMapping mapping;

    public TypeParser(IMapper mapper, IMapping mapping) {
        this.mapper = mapper;
        this.mapping = mapping;
    }

    public Object parse(Object value, Class valueClass, Class targetClass, boolean isAccessible, Converter converter) throws Exception {
        if (value == null) return null;
        // 先判断两个类型是否一致，如果一致，直接使用
        if (valueClass.equals(targetClass))
            return value;

        // 查看自定义转换器是否有匹配，如果有使用转换器转换
        if (converter == null)
            converter = mapping.getConverter(valueClass, targetClass);
        if (converter != null) {
            ConverterVerifyState verifyState = converter.verify(valueClass, targetClass);
            if (verifyState == ConverterVerifyState.Positive)
                return converter.toTarget(value);
            else if (verifyState == ConverterVerifyState.Negative)
                return converter.toSource(value);
        }


        // 如果目标类型是源类型的包装器,通过包装器类型的valueOf静态方法创建对象
        if (targetClass.equals(TypeUtiles.getWrapperClass(valueClass)))
            return targetClass.getDeclaredMethod("valueOf", valueClass).invoke(null, value);

        // 如果源类类型是包装器，目标类型是基础类型，可以直接返回
        if (valueClass.equals(TypeUtiles.getWrapperClass(targetClass)))
            return value;


        // 如果目标是字符串，直接使用toString返回
        if (targetClass.equals(String.class))
            return value == null ? null : value.toString();

        //如果目标类型是枚举，将值转换为字符串后用包装器valueOf的字符串方式创建对象
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
        Object object = mapper.map(value, targetClass, isAccessible);
        if (value != null && object == null)
            throw new ParseValueException("源类型：" + valueClass.getName() + "转换到目标类型：" + targetClass.getName() + "失败");
        return value;

    }
}
