package com.zhaofujun.automapper.builder;

import com.zhaofujun.automapper.mapping.ClassMapping;

public interface ClassMappingBuilder {
    ClassMappingBuilder field( String sourceFieldName,String targetFieldName);

    ClassMappingBuilder excludes(String[] targetFieldNames);

    ClassMapping builder();
}
