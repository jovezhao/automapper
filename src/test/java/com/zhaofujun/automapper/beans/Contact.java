package com.zhaofujun.automapper.beans;

public class Contact {
    private String address;
    private int tel;
    private transient  int aa;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }
}
