package com.zhaofujun.automapper.core;

import java.util.List;

public interface ClassMappingManager {
    void register(ClassMapping classMapping);

    ClassMapping create(Class sourceClass, Class targetClass);

    ClassMapping getClassMapping(Class sourceClass, Class targetClass);

    List<FieldMapping> getFieldMappings(Class sourceClass, Class targetClass,boolean isAccessible,InheritStrategy inheritStrategy);
}
