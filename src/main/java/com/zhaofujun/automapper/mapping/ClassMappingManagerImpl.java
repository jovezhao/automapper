package com.zhaofujun.automapper.mapping;

import com.zhaofujun.automapper.core.ClassMapping;
import com.zhaofujun.automapper.core.FieldMapping;
import com.zhaofujun.automapper.core.ClassMappingManager;
import com.zhaofujun.automapper.core.InheritStrategy;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 映射关系管理器，用于管理源对象、目标对象的关联关系。
 */
public class ClassMappingManagerImpl implements ClassMappingManager {
    private List<ClassMapping> classMappings = new CopyOnWriteArrayList<>();

    @Override
    public void register(ClassMapping classMapping) {
        this.classMappings.add(classMapping);
    }

    @Override
    public ClassMapping create(Class sourceClass, Class targetClass) {
        ClassMapping classMapping = getClassMapping(sourceClass, targetClass);
        if (classMapping == null)
            classMapping = new ClassMapping(sourceClass, targetClass);
        return classMapping;
    }

    @Override
    public ClassMapping getClassMapping(Class sourceClass, Class targetClass) {
        return classMappings.stream()
                .filter(p -> p.getSourceClass().equals(sourceClass) && p.getTargetClass().equals(targetClass))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<FieldMapping> getFieldMappings(Class sourceClass, Class targetClass, boolean isAccessible, InheritStrategy inheritStrategy) {
        // 默认使用目标优先
        ClassMapping classMapping = create(sourceClass, targetClass);
        Map<String,FieldMapping> allFieldMapping = classMapping.getAllFieldMapping();

        Stack<ClassMapping> classMappingStack = inheritStrategy.init(sourceClass, targetClass);
        while (!classMappingStack.empty()) {
            ClassMapping currentMapping = classMappingStack.pop();
            Map<String,FieldMapping> configurationFieldMapping = currentMapping.getConfigurationFieldMapping();
            allFieldMapping.putAll(configurationFieldMapping);
        }
        return allFieldMapping.values().stream().collect(Collectors.toList());
    }


}
