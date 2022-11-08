package com.jenga.weather.web.costVisualization.controller;

import com.jenga.weather.web.costVisualization.dto.CostByTimeDto;
import com.jenga.weather.web.costVisualization.service.CostVisualizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CostVisualizationController {
    private final CostVisualizationService costVisualizationService;

    @GetMapping("/cost/visualization")
    public String showInfraVisualization(Model model) {
        List<CostByTimeDto> costList = costVisualizationService.getCostByDate("2022-10-08", "2022-11-09");

        model.addAttribute("costList", costList);

        return "page/cost_visualization";
    }
}
