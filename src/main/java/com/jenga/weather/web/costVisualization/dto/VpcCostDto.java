package com.jenga.weather.web.costVisualization.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VpcCostDto {
    private String name;
    private String vpcId;
    private int cost;
    private int utilization;
}
