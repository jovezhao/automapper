package com.zhaofujun.automapper;

import com.zhaofujun.automapper.core.*;
import com.zhaofujun.automapper.mapping.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class AutoMapper implements IMapper, IMapping {
    private Logger logger = LoggerFactory.getLogger(AutoMapper.class);
    private ClassMappingManager classMappingManager = new ClassMappingManagerImpl();
    private ConverterManager converterManager = new ConverterManagerImpl();
    private TypeParser typeParser = new TypeParser(this, this);

    @Override
    public void map(Object source, Object target, boolean isAccessible, String... excludesTargetFieldNames) {

        List<FieldMapping> fieldMappingList = classMappingManager.getFieldMappings(source.getClass(), target.getClass(), isAccessible, new TargetFirstStrategy(classMappingManager));

        fieldMappingList.stream()
                .filter(p -> !p.getState().equals(MappingState.NO))
                .filter(p -> !Arrays.asList(excludesTargetFieldNames).contains(p.getTargetField().getMappingField()))
                .forEach(p -> {
                    try {
                        Object value = p.getSourceField().getValue(source);

                        Object newValue = typeParser.parse(value, p.getSourceField().getNextType(), p.getTargetField().getNextType(), isAccessible, p.getConverter());

                        p.getTargetField().setValue(target, newValue);

                    } catch (ParseValueException ex) {
                        logger.debug("映射{0}类型{1}字段失败,失败原因：{2}", target.getClass().getName(), p.getTargetField().getField().getName(), ex.getMessage());
                    } catch (Exception ex) {
                        logger.debug("映射{0}类型{1}字段失败,失败原因：{2}", target.getClass().getName(), p.getTargetField().getField().getName(), ex.getMessage());
                    }
                });

    }


    public IMapping registerConverter(Converter converter) {
        converterManager.register(converter);
        return this;
    }

    @Override
    public Converter getConverter(Class sourceClass, Class targetClass) {
        return converterManager.getConverter(sourceClass, targetClass);
    }


    @Override
    public IMapping registerClassMapping(ClassMapping classMapping, boolean duplex) {
        classMappingManager.register(classMapping);
        if (duplex) {
            ClassMapping reverseClassMapping = classMapping.reverse();
            classMappingManager.register(reverseClassMapping);
        }
        return this;
    }

}

