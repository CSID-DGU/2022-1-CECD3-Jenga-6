package com.jenga.weather.domain.nat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeNatGatewaysResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;

@Slf4j
public class NatTest {

    @Test
    void getDescription() {
        Region region = Region.AP_NORTHEAST_2;
        try (Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build()) {
            DescribeNatGatewaysResponse describeNatGatewaysResponse = ec2.describeNatGateways();
            describeNatGatewaysResponse.natGateways().forEach(v -> {
                log.info("NAT Gateway ID : " + v.natGatewayId());
                log.info("NAT Gateway's VPC ID : " + v.vpcId());
                log.info("NAT Gateway's Subnet ID : " + v.subnetId());
            });
        } catch (Ec2Exception e) {
            e.printStackTrace();
        }
    }
}

