package com.zhaofujun.automapper.core;

public interface IMapping {

    /**
     * 注册转换器
     * @param converter 转换器实例
     * @return
     */
    IMapping registerConverter(Converter converter);

    Converter getConverter(Class sourceClass, Class targetClass);

    //自动匹配所有带set的字段
    IMapping registerClassMapping(ClassMapping classMapping,boolean duplex);
}
