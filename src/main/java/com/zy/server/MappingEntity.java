package com.zy.server;

import java.util.HashSet;
import java.util.Set;

public class MappingEntity {
    public String name;
    public Set<String> pattern = new HashSet<>();
    @Override
    public String toString() {
        return "MappingEntity{" +
                "name='" + name + '\'' +
                ", pattern=" + pattern +
                '}';
    }
}