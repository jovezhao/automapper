package com.zhaofujun.automapper;

import com.zhaofujun.automapper.builder.ClassMappingBuilder;
import com.zhaofujun.automapper.builder.DefaultClassMappingBuilder;
import com.zhaofujun.automapper.mapping.ClassMappingManager;
import com.zhaofujun.automapper.mapping.FieldMapping;
import com.zhaofujun.automapper.reflect.BeanUtils;

import java.util.List;

public class AutoMapper implements IMapper {

    private ClassMappingManager classMappingManager = new ClassMappingManager();

    @Override
    public void map(Object source, Object target) {
        List<FieldMapping> fieldMappingList = classMappingManager.getFieldMappingList(source.getClass(), target.getClass());
        fieldMappingList.forEach(p -> {
            try {
                Object value = p.getSourceGetter().invoke(source);
                if (p.getTargetSetter() != null)
                    p.getTargetSetter().invoke(target, BeanUtils.parseValue(value, p.getSourceField().getType(), p.getTargetField().getType()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }

    @Override
    public <T> T map(Object source, Class<T> targetClass) {
        try {
            T target = targetClass.getConstructor().newInstance();
            map(source, target);
            return target;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public ClassMappingBuilder mapping(Class sourceClass, Class targetClass) {
        return mapping(sourceClass, targetClass, false);
    }

    @Override
    public ClassMappingBuilder mapping(Class sourceClass, Class targetClass, boolean allowPrivate) {

        ClassMappingBuilder classBuilder = new DefaultClassMappingBuilder(sourceClass, targetClass, allowPrivate);

        classMappingManager.addClassMapping(classBuilder.builder());

        return classBuilder;
    }
}

