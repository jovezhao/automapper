package com.zhaofujun.automapper.core;

public interface Converter<U, T> {

    Class<U> getSourceClass();

    Class<T> getTargetClass();

    T toTarget(U source);

    U toSource(T target);

    default ConverterVerifyState verify(Class sourceClass,Class targetClass){
        if(getSourceClass().equals(sourceClass) && getTargetClass().equals(targetClass))
            return ConverterVerifyState.Positive;
        if(getSourceClass().equals(targetClass)&& getTargetClass().equals(sourceClass))
            return ConverterVerifyState.Negative;
        return ConverterVerifyState.Fail;
    }

}
