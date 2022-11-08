package com.jenga.weather.web.infravisualization.node;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RootNode {

    private String id;
    private String name;
    private List<JSONObject> children;

    public RootNode() {
        this.id = "root";
        this.name = "root";
        this.children = new ArrayList<>();
    }

    public void addChildren(JSONObject jsonObject) {
        children.add(jsonObject);
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
