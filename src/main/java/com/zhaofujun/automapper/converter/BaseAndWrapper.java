package com.zhaofujun.automapper.converter;

public class BaseAndWrapper {
    private Class baseClass;
    private Class wrapperClass;

    public BaseAndWrapper(Class baseClass, Class wrapperClass) {
        this.baseClass = baseClass;
        this.wrapperClass = wrapperClass;
    }

    public Class getBaseClass() {
        return baseClass;
    }

    public Class getWrapperClass() {
        return wrapperClass;
    }
}

