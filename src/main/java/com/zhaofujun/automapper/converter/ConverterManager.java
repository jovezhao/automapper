package com.zhaofujun.automapper.converter;

import java.util.ArrayList;
import java.util.List;

public class ConverterManager {

    private List<Converter> converters = new ArrayList<>();


    public ConverterInfo getConverter(Class sourceClass, Class targetClass) {
        Converter converter = converters.stream()
                .filter(p -> (sourceClass.isAssignableFrom( p.getSourceClass())&& targetClass.isAssignableFrom(p.getTargetClass()))
                        ||
                        (targetClass.isAssignableFrom(p.getSourceClass()) && sourceClass.isAssignableFrom(p.getTargetClass()))
                )
                .findFirst()
                .orElse(null);
        if (converter == null) return null;
        if (converter.getTargetClass().equals(targetClass))
            return new ConverterInfo(converter, ConverterInfo.Direction.positive);
        else
            return new ConverterInfo(converter, ConverterInfo.Direction.negative);
    }

    public void addConverter(Converter converter) {
        converters.add(converter);
    }
}


