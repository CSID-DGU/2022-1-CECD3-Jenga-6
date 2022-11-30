package com.jenga.weather.web.costVisualization.controller;

import com.jenga.weather.web.costVisualization.dto.CostByRegionDto;
import com.jenga.weather.web.costVisualization.dto.CostByTimeDto;
import com.jenga.weather.web.costVisualization.service.CostVisualizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CostVisualizationController {
    private final CostVisualizationService costVisualizationService;

    @GetMapping("/cost/visualization/period")
    public String showInfraVisualizationByPeriod(Model model) {
//        List<CostByTimeDto> costList = costVisualizationService.getCostByDate("2022-10-01", "2022-11-01");

//        model.addAttribute("vpcCostList", costList);

        return "page/cost_visualization_period";
    }

    @GetMapping("/cost/visualization/region")
    public String showInfraVisualizationByRegion(Model model) {
//        List<CostByRegionDto> costList = costVisualizationService.getCostByRegion("2022-10-01", "2022-11-01");

//        model.addAttribute("vpcCostList", costList);

        return "page/cost_visualization_region";
    }

    @GetMapping("/cost/result")
    public ResponseEntity<List<CostByTimeDto>> getCost() {
        return ResponseEntity.ok(costVisualizationService.getCostByDate("2022-10-01", "2022-11-01"));
    }

    @GetMapping("/cost/result2")
    public ResponseEntity<List<CostByRegionDto>> getCost2() {
        return ResponseEntity.ok(costVisualizationService.getCostByRegion("2022-10-01", "2022-11-01"));
    }
}
