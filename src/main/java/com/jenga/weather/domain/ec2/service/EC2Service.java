package com.jenga.weather.domain.ec2.service;

import com.jenga.weather.domain.ec2.model.EC2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EC2Service {

    public List<EC2> getEC2Instances(Ec2Client ec2Client) {
        List<EC2> ec2List = new ArrayList<>();

        try {
            String nextToken = null;
            do {
                DescribeInstancesResponse response = ec2Client.describeInstances();
                for (Reservation reservation : response.reservations()) {
                    for (Instance instance : reservation.instances()) {
                        EC2 ec2 = EC2.builder()
                                .imageId((instance.imageId()))
                                .resourceId(instance.instanceId())
//                                .instanceId(instance.instanceId())
                                .instanceType(instance.instanceTypeAsString())
                                .keyName(instance.keyName())
                                .privateDnsName(instance.privateDnsName())
                                .privateIpAddress(instance.privateIpAddress())
                                .vpcId(instance.vpcId())
                                .subnetId(instance.subnetId())
                                .build();

                        ec2List.add(ec2);
                    }
                }
                nextToken = response.nextToken();
            } while (nextToken != null);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return ec2List;
    }
}
