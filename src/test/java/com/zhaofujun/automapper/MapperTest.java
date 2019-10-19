package com.zhaofujun.automapper;

public class MapperTest {

    public static void main(String[] args) {
        IMapper mapper = new AutoMapper();


        mapper.mapping(ClassA.class, ClassB.class)
                .field("b", "b1");

        ClassA classA = new ClassA();
        classA.setA("a");
        classA.setB(Boolean.TRUE);
        classA.setC(11);
        classA.setD(Byte.decode("2"));

        ClassB classB = mapper.map(classA, ClassB.class);

        System.out.println(classB);




    }
}

