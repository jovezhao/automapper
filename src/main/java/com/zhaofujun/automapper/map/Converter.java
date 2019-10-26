package com.zhaofujun.automapper.map;

public abstract class Converter<U, T> {

    protected abstract Class<U> getSourceClass();

    protected abstract Class<T> getTargetClass();

   public abstract T toTarget(U source);

   public abstract U toSource(T target);
}
