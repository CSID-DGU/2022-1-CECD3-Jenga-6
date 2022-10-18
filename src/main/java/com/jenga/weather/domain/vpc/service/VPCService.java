package com.jenga.weather.domain.vpc.service;

import com.jenga.weather.domain.vpc.model.VPC;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;


import java.util.ArrayList;
import java.util.List;

@Service
public class VPCService {
    public List<VPC> getVPCInstances(Ec2Client ec2Client) {
        List<VPC> vpcList = new ArrayList<>();

        try {
            DescribeVpcsResponse describeVpcsResponse = ec2Client.describeVpcs();
            describeVpcsResponse.vpcs().forEach(v -> {
                VPC vpc = VPC.builder()
                        .resourceId(v.vpcId())
                        .resourceName(!v.tags().isEmpty() ? v.tags().get(0).value() : "-")
                        .build();

                vpcList.add(vpc);
            });
        } catch (Ec2Exception e) {
            e.printStackTrace();
        }

        return vpcList;
    }
}
