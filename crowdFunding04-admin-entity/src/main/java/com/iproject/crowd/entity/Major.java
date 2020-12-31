package com.iproject.crowd.entity;

public class Major {

    private String name;

    public Major() {
    }

    public Major(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Major{" +
                "name='" + name + '\'' +
                '}';
    }
}
