package com.jenga.weather.web.infravisualization.node;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Graph {
    private List<JSONObject> links;
    private RootNode root;

    public Graph() {
        this.links = new ArrayList<>();
        this.root = null;
    }

    public void addLink(JSONObject jsonObject) {
        this.links.add(jsonObject);
    }

    public void setRoot(RootNode root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "links=" + links +
                ", root=" + root +
                '}';
    }
}
