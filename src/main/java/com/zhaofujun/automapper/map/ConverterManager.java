package com.zhaofujun.automapper.map;

import java.util.ArrayList;
import java.util.List;

public class ConverterManager {

    private List<Converter> converters = new ArrayList<>();


    public ConverterInfo getConverter(Class sourceClass, Class targetClass) {
        Converter converter = converters.stream()
                .filter(p -> (p.getSourceClass().isAssignableFrom(sourceClass) && p.getTargetClass().isAssignableFrom(targetClass))
                        ||
                        (p.getSourceClass().isAssignableFrom(targetClass) && p.getTargetClass().isAssignableFrom(sourceClass))
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


