package com.zhaofujun.automapper.core;

import com.zhaofujun.automapper.mapping.FieldInfoFactory;
import com.zhaofujun.automapper.mapping.NotFoundFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldMapping {


    private Logger logger = LoggerFactory.getLogger(FieldMapping.class);


    private FieldInfo sourceField; //源字段
    private FieldInfo targetField; //目标字段
    private Converter converter;
    private MappingState state;
    private ClassMapping classMapping;


    public FieldMapping(FieldInfo targetField, ClassMapping classMapping) {
        this.targetField = targetField;
        this.classMapping = classMapping;
        this.state = MappingState.YES;
    }

    public void mapping(FieldInfo sourceField, Converter converter) {
        this.sourceField = sourceField;
        this.converter = converter;
    }

    public void mapping(FieldInfo sourceField) {
        mapping(sourceField, null);
    }

    public void exclude() {
        this.state = MappingState.NO;
        if (this.sourceField == null) {
            try {
                this.sourceField = FieldInfoFactory.create(classMapping.getSourceClass(), targetField.getMappingField());
            } catch (NotFoundFieldException exception) {
                logger.warn("排除字段时没有找到源字段" + targetField.getMappingField());
            }
        }
    }


    public FieldInfo getSourceField() {
        return sourceField;
    }

    public FieldInfo getTargetField() {
        return targetField;
    }

    public Converter getConverter() {
        return converter;
    }

    public MappingState getState() {
        return state;
    }
}
