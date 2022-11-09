package com.jenga.weather.web.infravisualization.node;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RootNode {

    private String id;
    private String name;
    private List<Object> children;

    public RootNode() {
        this.id = "root";
        this.name = "root";
        this.children = new ArrayList<>();
    }

    public void addChildren(Object object) {
        children.add(object);
    }

    @Override
    public String toString() {
        return "RootNode{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", children=" + children +
                '}';
    }
}
