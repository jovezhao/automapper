package com.zhaofujun.automapper.mapping;

import com.zhaofujun.automapper.core.Converter;
import com.zhaofujun.automapper.core.ConverterManager;

import java.util.ArrayList;
import java.util.List;

public class ConverterManagerImpl implements ConverterManager {

    private List<Converter> converters = new ArrayList<>();


    @Override
    public Converter getConverter(Class sourceClass, Class targetClass) {
        return converters.stream()
                .filter(p -> (p.getSourceClass().equals(sourceClass) && p.getTargetClass().equals(targetClass))
                        ||
                        (p.getSourceClass().equals(targetClass) && p.getTargetClass().equals(sourceClass))
                )
                .findFirst()
                .orElse(null);
    }

    @Override
    public void register(Converter converter) {
        converters.add(converter);
    }
}


