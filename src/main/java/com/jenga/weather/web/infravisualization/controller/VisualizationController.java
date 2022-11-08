package com.jenga.weather.web.infravisualization.controller;

import com.jenga.weather.web.infravisualization.service.VisualizationService;
import lombok.RequiredArgsConstructor;
import com.jenga.weather.web.infravisualization.dto.VpcDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VisualizationController {

    private final VisualizationService visualizationService;

    @GetMapping("/infra/visualization")
    public String showInfraVisualization(Model model) {
        List<VpcDto> vpcList = new ArrayList<>();
        vpcList.add(new VpcDto("vpc_name_1", "vpc_id_1"));
        vpcList.add(new VpcDto("vpc_name_2", "vpc_id_2"));
        vpcList.add(new VpcDto("vpc_name_3", "vpc_id_3"));
        vpcList.add(new VpcDto("vpc_name_4", "vpc_id_4"));
        
        String result = visualizationService.mapInfra();

        String data = visualizationService.getData();
        log.info(data);
        model.addAttribute("visualization", data);
        model.addAttribute("vpcList", vpcList);
        
        return "page/infra_visualization";
    }
}