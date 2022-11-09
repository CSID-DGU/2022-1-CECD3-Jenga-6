package com.jenga.weather.web.infravisualization.node.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class VpcNode {
    private String id;
    private String name;
    private String href;
    private String resource_name;
    private int size;

    @Builder.Default
    private List<Object> children = new ArrayList<>();

    public void addChildren(Object object) {
        this.children.add(object);
    }
}
