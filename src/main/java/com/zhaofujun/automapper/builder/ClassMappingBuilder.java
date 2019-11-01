package com.zhaofujun.automapper.builder;

import com.zhaofujun.automapper.converter.Converter;
import com.zhaofujun.automapper.mapping.ClassMapping;

public interface ClassMappingBuilder {
    ClassMappingBuilder field( String sourceFieldName,String targetFieldName);
    ClassMappingBuilder field(String sourceFieldName, String targetFieldName, Converter converter);

    ClassMappingBuilder excludes(String... targetFieldNames);

    ClassMapping getClassMapping();
}
