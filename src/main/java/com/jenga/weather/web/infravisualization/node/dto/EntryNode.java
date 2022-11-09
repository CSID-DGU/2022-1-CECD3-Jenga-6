package com.jenga.weather.web.infravisualization.node.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class EntryNode {

    private String destination_cidr_block;
    private String state;
    private String nat_gateway_id;
    private String gateway_id;
    private String instance_id;
    private String network_interface_id;
}
