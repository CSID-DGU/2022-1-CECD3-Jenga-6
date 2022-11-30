package com.jenga.weather.web.costVisualization.service;

import com.jenga.weather.web.costVisualization.dto.CostByRegionDto;
import com.jenga.weather.web.costVisualization.dto.CostByTimeDto;
import com.jenga.weather.web.costVisualization.dto.CostDto;
import com.jenga.weather.web.costVisualization.dto.CostDto2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.costexplorer.model.*;
import software.amazon.awssdk.services.costexplorer.CostExplorerClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CostVisualizationService {

    public List<CostByTimeDto> getCostByDate(String startDate, String endDate) {
        GetCostAndUsageRequest costAndUsageRequest = GetCostAndUsageRequest.builder()
                .timePeriod(DateInterval.builder()
                        .start(startDate)
                        .end(endDate)
                        .build())
                .granularity("DAILY")
                .metrics("BLENDED_COST", "USAGE_QUANTITY")
                .groupBy(GroupDefinition.builder()
                        .type("DIMENSION")
                        .key("SERVICE")
                        .build())
                .build();
        CostExplorerClient costExplorerClient = CostExplorerClient.builder()
                .region(Region.AWS_GLOBAL)
                .build();

        List<ResultByTime> resultByTimeList = costExplorerClient.getCostAndUsage(costAndUsageRequest).resultsByTime();
        List<CostByTimeDto> costByTimeDtoList = new ArrayList<>();
        double totalCost, totalUtilization;
        for (ResultByTime result : resultByTimeList) {
            List<CostDto> costDtoList = new ArrayList<>();
            setInstanceName(costDtoList);
            totalCost = 0;
            totalUtilization = 0;
            for (Group group : result.groups()) {
                for (CostDto costDto : costDtoList) {
                    if (group.keys().get(0).equals(costDto.getInstanceName())) {
                        costDto.updateCostAndUtilization(Double.parseDouble(group.metrics().get("BlendedCost").amount()),
                                Double.parseDouble(group.metrics().get("UsageQuantity").amount()));
                        totalCost += Double.parseDouble(group.metrics().get("BlendedCost").amount());
                        totalUtilization += Double.parseDouble(group.metrics().get("UsageQuantity").amount());
                    }
                }
            }
            costByTimeDtoList.add(
                    CostByTimeDto.builder()
                            .date(result.timePeriod().start())
                            .totalCost(totalCost)
                            .totalUtilization(totalUtilization)
                            .costDtoList(costDtoList)
                            .build());
        }
        return costByTimeDtoList;
    }

    public List<CostByRegionDto> getCostByRegion(String startDate, String endDate) {
        GetCostAndUsageRequest costAndUsageRequest = GetCostAndUsageRequest.builder()
                .timePeriod(DateInterval.builder()
                        .start(startDate)
                        .end(endDate)
                        .build())
                .granularity("DAILY")
                .metrics("BLENDED_COST", "USAGE_QUANTITY")
                .groupBy(GroupDefinition.builder()
                        .type("DIMENSION")
                        .key("REGION")
                        .build())
                .build();
        CostExplorerClient costExplorerClient = CostExplorerClient.builder()
                .region(Region.AWS_GLOBAL)
                .build();

        List<ResultByTime> resultByRegionList = costExplorerClient.getCostAndUsage(costAndUsageRequest).resultsByTime();
        List<CostByRegionDto> costByRegionDtoList = new ArrayList<>();
        double totalCost, totalUtilization;
        for (ResultByTime result : resultByRegionList) {
            List<CostDto2> costDtoList = new ArrayList<>();
            totalCost = 0;
            totalUtilization = 0;
            for (Group group : result.groups()) {
                costDtoList.add(
                        CostDto2.builder()
                                .regionName(group.keys().get(0))
                                .cost(Double.parseDouble(group.metrics().get("BlendedCost").amount()))
                                .utilization(Double.parseDouble(group.metrics().get("UsageQuantity").amount()))
                                .build());
                totalCost += Double.parseDouble(group.metrics().get("BlendedCost").amount());
                totalUtilization += Double.parseDouble(group.metrics().get("UsageQuantity").amount());
            }

            costByRegionDtoList.add(
                    CostByRegionDto.builder()
                            .date(result.timePeriod().start())
                            .totalCost(totalCost)
                            .totalUtilization(totalUtilization)
                            .costDtoList(costDtoList)
                            .build());
        }

        return costByRegionDtoList;
    }

    public void setInstanceName(List<CostDto> costDtoList) {
        costDtoList.add(CostDto.builder()
                .instanceName("EC2 - Other")
                .build());
        costDtoList.add(CostDto.builder()
                .instanceName("Amazon Elastic Compute Cloud - Compute")
                .build());
        costDtoList.add(CostDto.builder()
                .instanceName("Amazon Relational Database Service")
                .build());
        costDtoList.add(CostDto.builder()
                .instanceName("Amazon Simple Storage Service")
                .build());
        costDtoList.add(CostDto.builder()
                .instanceName("Amazon Route 53")
                .build());
        costDtoList.add(CostDto.builder()
                .instanceName("Tax")
                .build());
    }
}
