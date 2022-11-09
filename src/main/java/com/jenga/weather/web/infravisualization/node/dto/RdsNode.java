package com.jenga.weather.web.infravisualization.node.dto;

import com.jenga.weather.web.infravisualization.node.LeafNode;
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
public class RdsNode {
    private String id;
    private String name;
    private String href;
    private String engine;
    private String vpc_id;
    private String subnet_id;
    private String status;
    private String db_instance_class;
    private String region;

    @Builder.Default
    private List<Object> children = new ArrayList<>();

    public void addChildren(Object object) {
        this.children.add(object);
    }
}
