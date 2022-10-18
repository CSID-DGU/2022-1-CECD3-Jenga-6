package com.jenga.weather.domain.igw.service;

import com.jenga.weather.domain.igw.model.IGW;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInternetGatewaysResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;

import java.util.ArrayList;
import java.util.List;

@Service
public class IGWService {

    public List<IGW> getIGWInstances(Ec2Client ec2Client) {
        List<IGW> igwList = new ArrayList<>();

        try {
            DescribeInternetGatewaysResponse response = ec2Client.describeInternetGateways();
            response.internetGateways().forEach(v -> {
                IGW igw = IGW.builder()
                        .resourceId(v.internetGatewayId())
                        .resourceName(!v.tags().isEmpty() ? v.tags().get(0).value() : "-")
                        .ownerId(v.ownerId())
                        .vpcId(v.attachments().get(0).vpcId())
                        .vpcState(v.attachments().get(0).stateAsString())
                        .build();

                igwList.add(igw);
            });
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return igwList;
    }
}
