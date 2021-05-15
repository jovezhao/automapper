//package com.zhaofujun.automapper.mapping;
//
//import com.zhaofujun.automapper.core.Converter;
//
//public class ConverterInfo {
//
//    public enum Direction {
//        /**
//         * 正向
//         */
//        positive,
//        /**
//         * 反向
//         */
//        negative
//    }
//
//    private Converter converter;
//    private Direction direction;
//
//    public ConverterInfo(Converter converter, Direction direction) {
//        this.converter = converter;
//        this.direction = direction;
//    }
//
//    public Converter getConverter() {
//        return converter;
//    }
//
//    public Direction getDirection() {
//        return direction;
//    }
//
//    public Object convert(Object source) {
//        if (direction == Direction.negative)
//            return this.converter.toSource(source);
//        return this.converter.toTarget(source);
//    }
//}
