package com.jenga.weather.domain.routeTable.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeRouteTablesResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.RouteTable;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RouteTableService {

    public List<RouteTable> getRouteTableInstances(Ec2Client ec2Client) {
        List<RouteTable> routeTableList = new ArrayList<>();

        try {
            DescribeRouteTablesResponse describeRouteTablesResponse = ec2Client.describeRouteTables();
            describeRouteTablesResponse.routeTables().forEach(v -> {
                routeTableList.add(v);
            });
        }catch (Ec2Exception e) {
            e.printStackTrace();
        }
        return routeTableList;
    }
}
