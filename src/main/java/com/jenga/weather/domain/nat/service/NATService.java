package com.jenga.weather.domain.nat.service;

import com.jenga.weather.domain.nat.model.NAT;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeNatGatewaysResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;

import java.util.ArrayList;
import java.util.List;

@Service
public class NATService {
    public List<NAT> getNATInstances(Ec2Client ec2Client) {
        List<NAT> natList = new ArrayList<>();

        try {
            DescribeNatGatewaysResponse describeNatGatewaysResponse = ec2Client.describeNatGateways();
            describeNatGatewaysResponse.natGateways().forEach(v -> {
                NAT nat = NAT.builder()
                        .resourceId(v.natGatewayId())
                        .resourceName(!v.tags().isEmpty() ? v.tags().get(0).value() : "-")
                        .subnetId(v.subnetId())
                        .vpcId(v.vpcId())
                        .build();

                natList.add(nat);
            });
        } catch (Ec2Exception e) {
            e.printStackTrace();
        }
        return natList;
    }
}
