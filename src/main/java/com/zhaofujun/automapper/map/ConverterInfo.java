package com.zhaofujun.automapper.map;

public class ConverterInfo {

   public enum Direction {positive,     negative}

    private Converter converter;
    private Direction direction;

    public ConverterInfo(Converter converter, Direction direction) {
        this.converter = converter;
        this.direction = direction;
    }

    public Converter getConverter() {
        return converter;
    }

    public Direction getDirection() {
        return direction;
    }
}
