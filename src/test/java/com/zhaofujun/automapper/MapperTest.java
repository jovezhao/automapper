package com.zhaofujun.automapper;

public class MapperTest {

    public static void main(String[] args) {
        IMapper mapper = new AutoMapper();


        mapper.mapping(ClassA.class, ClassB.class)
                .field("a1", "b1");

        ClassA classA = new ClassA();
        classA.setA("a");
        classA.setA1(Boolean.TRUE);
        classA.setA2(true);

        ClassB classB = mapper.map(classA, ClassB.class);

        System.out.println(classB);
    }
}

