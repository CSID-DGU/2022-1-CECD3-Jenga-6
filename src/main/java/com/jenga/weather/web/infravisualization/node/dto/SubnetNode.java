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
public class SubnetNode {

    private String id;
    private String name;
    private String href;
    private int size;
    private String vpc_id;
    private String resource_name;

    @Builder.Default
    private List<Object> children = new ArrayList<>();

    @Builder.Default
    private List<Object> route_table = new ArrayList<>();

    public void addChildren(Object object) {
        this.children.add(object);
    }

    public void addRouteTable(Object object) {
        this.route_table.add(object);
    }
}
