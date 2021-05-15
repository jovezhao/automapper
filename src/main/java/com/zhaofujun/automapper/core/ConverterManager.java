package com.zhaofujun.automapper.core;


public interface ConverterManager {
    Converter getConverter(Class sourceClass, Class targetClass);

    void register(Converter converter);
}
