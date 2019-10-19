package com.zhaofujun.automapper;

public class ClassA {
    private String a;
    private Boolean a1;
    private boolean a2;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public Boolean getA1() {
        return a1;
    }

    public void setA1(Boolean a1) {
        this.a1 = a1;
    }

    public boolean isA2() {
        return a2;
    }

    public void setA2(boolean a2) {
        this.a2 = a2;
    }

    @Override
    public String toString() {
        return "ClassA{" +
                "a='" + a + '\'' +
                ", a1='" + a1 + '\'' +
                '}';
    }
}
