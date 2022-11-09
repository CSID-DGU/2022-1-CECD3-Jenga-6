package com.jenga.weather.web.costVisualization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CostByTimeDto {
    private String date;
    private double totalCost;
    private double totalUtilization;
    private List<CostDto> costDtoList;
}
