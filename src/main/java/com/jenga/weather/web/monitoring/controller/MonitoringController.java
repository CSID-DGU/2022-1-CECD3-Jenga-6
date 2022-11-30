package com.jenga.weather.web.monitoring.controller;

import com.jenga.weather.web.monitoring.dto.MonitoringDto;
import com.jenga.weather.web.monitoring.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MonitoringController {
    private final MonitoringService monitoringService;
    CloudWatchClient cwc = CloudWatchClient.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(ProfileCredentialsProvider.create())
            .build();

    @GetMapping("/monitoring")
    public List<MonitoringDto> getMetrics() {
        return monitoringService.getMetrics(cwc);
    }
}
