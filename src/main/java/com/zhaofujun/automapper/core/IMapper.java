package com.zhaofujun.automapper.core;

public interface IMapper extends IMapping {
    /**
     * 映射对象<br/>
     * 默认不支持不包含getter,setter的属性<br/>
     * 映射时指定的排除集合只对本次映射上下文<br/>
     * @param source 源对象
     * @param target 目标对象
     * @param excludesTargetFieldNames 可排除的目标属性集
     */
    default void map(Object source, Object target, String... excludesTargetFieldNames) {
        map(source, target, false, excludesTargetFieldNames);
    }

    /**
     * 映射对象<br/>
     * 目标类型需要支持默认无参构造函数<br/>
     * 默认不支持不包含getter,setter的属性<br/>
     * 映射时指定的排除集合只对本次映射上下文<br/>
     * @param source 元对象
     * @param targetClass 目标类型
     * @param excludesTargetFieldNames 可排除的目标属性集
     * @param <T> 目标泛型类型
     * @return 目标对象
     */
    default <T> T map(Object source, Class<T> targetClass, String... excludesTargetFieldNames) {
        return map(source, targetClass, false, excludesTargetFieldNames);
    }

    /**
     * 映射对象<br/>
     * 目标类型需要支持默认无参构造函数<br/>
     * 映射时指定的排除集合只对本次映射上下文<br/>
     * @param source 源对象
     * @param targetClass 目标类型
     * @param isAccessible true时支持没有getter，setter的属性映射
     * @param excludesTargetFieldNames 可排除的目标属性集
     * @param <T> 目标泛型类型
     * @return 目标对象
     */
    default  <T> T map(Object source, Class<T> targetClass, boolean isAccessible, String... excludesTargetFieldNames){
        try {
            T target = targetClass.getConstructor().newInstance();
            map(source, target, isAccessible, excludesTargetFieldNames);
            return target;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 映射对象<br/>
     * 映射时指定的排除集合只对本次映射上下文<br/>
     * @param source 源对象
     * @param target 目标对象
     * @param isAccessible true时支持没有getter，setter的属性映射
     * @param excludesTargetFieldNames 可排除的目标属性集
     */
    void map(Object source, Object target, boolean isAccessible, String... excludesTargetFieldNames);

}
