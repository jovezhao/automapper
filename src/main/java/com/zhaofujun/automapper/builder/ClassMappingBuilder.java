package com.zhaofujun.automapper.builder;

import com.zhaofujun.automapper.mapping.ClassMapping;
import com.zhaofujun.automapper.mapping.NotFoundFieldException;

public interface ClassMappingBuilder {
    ClassMappingBuilder field( String sourceFieldName,String targetFieldName);

    ClassMappingBuilder excludes(String... targetFieldNames);

    ClassMapping getClassMapping();
}
