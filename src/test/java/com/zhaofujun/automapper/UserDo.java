package com.zhaofujun.automapper;

public class UserDo {
    enum  Sex{
        female,male
    }
    private String id;
    private Sex sex;
    private int age;
    private String name;
    private Contact contact;

    public String getId() {
        return id;
    }


    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}

