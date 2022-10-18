package com.jenga.weather.web.infravisualization.controller;

import com.jenga.weather.web.infravisualization.dto.VpcDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class VisualizationController {


    @GetMapping("/infra/visualization")
    public String showInfraVisualization(Model model) {
        List<VpcDto> vpcList = new ArrayList<>();
        vpcList.add(new VpcDto("vpc_name_1", "vpc_id_1"));
        vpcList.add(new VpcDto("vpc_name_2", "vpc_id_2"));
        vpcList.add(new VpcDto("vpc_name_3", "vpc_id_3"));
        vpcList.add(new VpcDto("vpc_name_4", "vpc_id_4"));

        model.addAttribute("vpcList", vpcList);
        return "page/infra_visualization";
    }
}
