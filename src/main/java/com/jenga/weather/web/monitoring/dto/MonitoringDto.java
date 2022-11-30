package com.jenga.weather.web.monitoring.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class MonitoringDto {
    private String label;
    private List<Instant> timestamps;
    private List<Double> values;
}
