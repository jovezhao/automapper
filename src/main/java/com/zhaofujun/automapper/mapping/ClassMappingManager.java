package com.zhaofujun.automapper.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 映射关系管理器，用于管理源对象、目标对象的关联关系。
 */
public class ClassMappingManager {
    List<ClassMapping> classMappings = new CopyOnWriteArrayList<>();

    public void registerClassMapping(ClassMapping classMapping) {
        this.classMappings.add(classMapping);
    }

    //按源类型和目标类型获取字段列表，如果没有获取到则按
    public List<FieldMapping> getFieldMappingList(Class sourceClass, Class targetClass ,boolean allowNoSetter) {


        ClassMapping classMapping = classMappings.stream()
                .filter(p -> p.getSourceClass().isAssignableFrom(sourceClass) && p.getTargetClass().isAssignableFrom(targetClass))
                .findFirst()
                .orElse(null);
        if (classMapping == null) {
            classMapping = new ClassMapping(sourceClass, targetClass);
            classMappings.add(classMapping);
        }
        return classMapping.getEffectiveFieldMappingList(allowNoSetter);
    }
}
