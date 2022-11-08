package com.jenga.weather.web.infravisualization.node;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeafNode {

    private String id;
    private String name;
    private List<JSONObject> children;
    private double size;

    public LeafNode() {
        this.id = null;
        this.name = null;
        this.children = new ArrayList<>();
        this.size = 0.01;
    }

    @Override
    public String toString() {
        return "LeafNode{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", children=" + children +
                ", size=" + size +
                '}';
    }
}
