package com.jenga.weather.web.infravisualization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class VpcDto {

    private String name;
    private String vpcId;
}
