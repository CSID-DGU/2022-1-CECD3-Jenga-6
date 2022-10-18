package com.jenga.weather.web.costVisualization.controller;

import com.jenga.weather.web.costVisualization.dto.VpcCostDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CostVisualizationController {


    @GetMapping("/cost/visualization")
    public String showInfraVisualization(Model model) {

        List<VpcCostDto> vpcCostList = new ArrayList<>();
        vpcCostList.add(new VpcCostDto("vpc_name_1", "vpc_id_1", 1444, 20));
        vpcCostList.add(new VpcCostDto("vpc_name_2", "vpc_id_2", 14444, 70));
        vpcCostList.add(new VpcCostDto("vpc_name_3", "vpc_id_3", 5522, 10));
        vpcCostList.add(new VpcCostDto("vpc_name_4", "vpc_id_4", 66444, 10));

        model.addAttribute("vpcCostList", vpcCostList);
        return "page/cost_visualization";
    }
}
