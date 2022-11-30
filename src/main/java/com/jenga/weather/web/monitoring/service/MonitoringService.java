package com.jenga.weather.web.monitoring.service;

import com.jenga.weather.web.monitoring.dto.MonitoringDto;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class MonitoringService {
    public List<MonitoringDto> getMetrics(CloudWatchClient client) {
        Instant start = Instant.now().truncatedTo(ChronoUnit.MILLIS).minusSeconds(604800);
        Instant end = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        Dimension dimension = Dimension.builder()
                .name("InstanceId")
                .value("i-047252dbe7544c271")
                .build();
        List<Metric> metricList = new ArrayList<>();
        metricList.add(Metric.builder()
                .dimensions(dimension)
                .namespace("AWS/EC2")
                .metricName("CPUUtilization")
                .build());
        metricList.add(Metric.builder()
                .dimensions(dimension)
                .namespace("AWS/EC2")
                .metricName("NetworkIn")
                .build());
        metricList.add(Metric.builder()
                .dimensions(dimension)
                .namespace("AWS/EC2")
                .metricName("NetworkOut")
                .build());
        metricList.add(Metric.builder()
                .dimensions(dimension)
                .namespace("AWS/EC2")
                .metricName("DiskReadOps")
                .build());
        metricList.add(Metric.builder()
                .dimensions(dimension)
                .namespace("AWS/EC2")
                .metricName("DiskWriteOps")
                .build());
        List<MetricStat> metricStatList = new ArrayList<>();
        for (Metric metric : metricList) {
            metricStatList.add(MetricStat.builder()
                    .stat("Sum")
                    .period(86400)
                    .metric(metric)
                    .build());
        }

        List<MetricDataQuery> metricDataQueryList = new ArrayList<>();
        for (int i = 0; i < metricStatList.size(); i++) {
            metricDataQueryList.add(MetricDataQuery.builder()
                    .metricStat(metricStatList.get(i))
                    .id("q" + i)
                    .returnData(true)
                    .build());
        }

        List<GetMetricDataRequest> dataRequestList = new ArrayList<>();
        for (MetricDataQuery metricDataQuery : metricDataQueryList) {
            dataRequestList.add(GetMetricDataRequest.builder()
                    .maxDatapoints(100)
                    .startTime(start)
                    .endTime(end)
                    .metricDataQueries(metricDataQuery)
                    .build());
        }

        List<MonitoringDto> monitoringDtoList = new ArrayList<>();
        for (GetMetricDataRequest request : dataRequestList) {
            GetMetricDataResponse response = client.getMetricData(request);
            List<MetricDataResult> data = response.metricDataResults();
            for (MetricDataResult item : data) {
                monitoringDtoList.add(MonitoringDto.builder()
                        .label(item.label())
                        .timestamps(item.timestamps())
                        .values(item.values())
                        .build());
            }
        }
        return monitoringDtoList;
    }
}
