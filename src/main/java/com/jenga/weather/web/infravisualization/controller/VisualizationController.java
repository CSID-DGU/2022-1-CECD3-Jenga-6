package com.jenga.weather.web.infravisualization.controller;

import com.jenga.weather.web.infravisualization.dto.VpcDto;
import com.jenga.weather.web.infravisualization.node.Graph;
import com.jenga.weather.web.infravisualization.service.VisualizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VisualizationController {

    private final VisualizationService visualizationService;

    @GetMapping("/infra/visualization")
    public String showInfraVisualization(Model model, @RequestParam(value = "vpcId", required = false) String vpcId) {

        List<VpcDto> vpcs = visualizationService.getVpcs();

        model.addAttribute("vpcs", vpcs);

        return "page/infra_visualization";
    }

    @GetMapping("/fetch_infra")
    public ResponseEntity<Graph> fetchInfra(@RequestParam(value = "vpcId", required = false) String vpcId) {


        return ResponseEntity.ok(visualizationService.getInstances(vpcId));
    }
}