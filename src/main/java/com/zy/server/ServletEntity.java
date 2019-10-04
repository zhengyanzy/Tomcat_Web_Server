package com.zy.server;

public class ServletEntity {
    public String name;
    public String className;
    @Override
    public String toString() {
        return "ServletEntity{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
