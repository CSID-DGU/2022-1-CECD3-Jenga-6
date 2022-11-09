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
public class NatNode {

    private String id;
    private String name;
    private String href;
    private String vpc_id;
    private String subnet_id;
    private String private_ip;
    private String public_ip;

    @Builder.Default
    private List<Object> children = new ArrayList<>();

    public void addChildren(Object object) {
        this.children.add(object);
    }
}
