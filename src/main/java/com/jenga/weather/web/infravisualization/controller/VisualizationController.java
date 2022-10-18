package com.jenga.weather.web.infravisualization.controller;

import com.jenga.weather.web.infravisualization.service.VisualizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor

@RequestMapping("/visualization")
@Controller
public class VisualizationController {

    private final VisualizationService visualizationService;

    @GetMapping("")
    public String visualizationInfra(Model model) {
        String result = visualizationService.mapInfra();
        model.addAttribute("visualization", result);
        return "visualization/Form";
    }

}
