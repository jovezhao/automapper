package com.zhaofujun.automapper.beans;

public class TeacherPO {
    private String name;
    private String age;
    private String stateLabel;
    private String parentName;
    private String parentAge;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentAge() {
        return parentAge;
    }

    public void setParentAge(String parentAge) {
        this.parentAge = parentAge;
    }

    public String getStateLabel() {
        return stateLabel;
    }

    public void setStateLabel(String stateLabel) {
        this.stateLabel = stateLabel;
    }
}
