package com.zhaofujun.automapper.beans;

public class Teacher {
    private String name;
    private int age;
    private Teacher parent;
    private State state;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Teacher getParent() {
        return parent;
    }

    public void setParent(Teacher parent) {
        this.parent = parent;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


}
