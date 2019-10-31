package com.zhaofujun.automapper.map;

public abstract class Converter<U, T> {

    public abstract Class<U> getSourceClass();

    public abstract Class<T> getTargetClass();

    public abstract T toTarget(U source);

    public abstract U toSource(T target);
}
