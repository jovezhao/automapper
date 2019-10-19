package com.zhaofujun.automapper;

public class ClassB {
    private String a;
    private String b1;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB1() {
        return b1;
    }

    public void setB1(String b1) {
        this.b1 = b1;
    }

    @Override
    public String toString() {
        return "ClassB{" +
                "a='" + a + '\'' +
                ", b1='" + b1 + '\'' +
                '}';
    }
}
