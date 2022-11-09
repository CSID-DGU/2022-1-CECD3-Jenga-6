package com.jenga.weather.web.costVisualization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CostDto {
    private String instanceName;
    private double cost = 0;
    private double utilization = 0;

    public void updateCostAndUtilization(double cost, double utilization) {
        this.cost = cost;
        this.utilization = utilization;
    }
}
