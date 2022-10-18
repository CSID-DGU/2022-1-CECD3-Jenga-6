package com.jenga.weather.domain.subnet.service;

import com.jenga.weather.domain.subnet.model.Subnet;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeSubnetsResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubnetService {
    public List<Subnet> getSubnetInstances(Ec2Client ec2Client) {
        List<Subnet> subnetList = new ArrayList<>();

        try {
            DescribeSubnetsResponse describeSubnetsResponse = ec2Client.describeSubnets();
            describeSubnetsResponse.subnets().forEach(v -> {
                Subnet subnet = Subnet.builder()
                        .resourceId(v.subnetId())
                        .resourceName(!v.tags().isEmpty() ? v.tags().get(0).value() : "-")
                        .vpcId(v.vpcId()).build();

                subnetList.add(subnet);
            });
        } catch (Ec2Exception e) {
            e.printStackTrace();
        }

        return subnetList;
    }
}
