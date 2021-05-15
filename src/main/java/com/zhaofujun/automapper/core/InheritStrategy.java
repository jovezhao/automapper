package com.zhaofujun.automapper.core;

import java.util.Stack;

/**
 * 继承策略
 */
public interface InheritStrategy {
    Stack<ClassMapping> init(Class sourceClass,Class TargetClass);
}
