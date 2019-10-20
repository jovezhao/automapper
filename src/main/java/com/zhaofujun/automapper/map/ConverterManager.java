package com.zhaofujun.automapper.map;

import java.util.ArrayList;
import java.util.List;

public class ConverterManager {

    private static List<Converter> converters = new ArrayList<>();


    public static ConverterInfo getConverter(Class sourceClass, Class targetClass) {
        Converter converter = converters.stream()
                .filter(p -> (p.getSourceClass().equals(sourceClass) && p.getTargetClass().equals(targetClass))
                        ||
                        (p.getSourceClass().equals(targetClass) && p.getTargetClass().equals(sourceClass))
                )
                .findFirst()
                .orElse(null);
        if (converter == null) return null;
        if (converter.getTargetClass().equals(targetClass))
            return new ConverterInfo(converter, ConverterInfo.Direction.positive);
        else
            return new ConverterInfo(converter, ConverterInfo.Direction.negative);
    }
}


