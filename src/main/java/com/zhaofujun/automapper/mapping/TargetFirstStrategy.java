package com.zhaofujun.automapper.mapping;

import com.zhaofujun.automapper.core.ClassMapping;
import com.zhaofujun.automapper.core.ClassMappingManager;
import com.zhaofujun.automapper.core.InheritStrategy;

import java.util.Stack;

public class TargetFirstStrategy implements InheritStrategy {

    private ClassMappingManager mappingManager;

    public TargetFirstStrategy(ClassMappingManager mappingManager) {
        this.mappingManager = mappingManager;
    }

    @Override
    public Stack<ClassMapping> init(Class sourceClass, Class targetClass) {
        Stack<ClassMapping> classMappings = new Stack<>();

        while (!Object.class.equals(targetClass)) {
            while (!Object.class.equals(sourceClass)) {
                ClassMapping classMapping = mappingManager.getClassMapping(sourceClass, targetClass);
                if (classMapping != null)
                    classMappings.push(classMapping);
                sourceClass = sourceClass.getSuperclass();
            }
            targetClass = targetClass.getSuperclass();
        }
        return classMappings;
    }
}
