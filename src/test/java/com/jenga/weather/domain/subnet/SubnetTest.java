package com.jenga.weather.domain.subnet;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeSubnetsResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;

@Slf4j
public class SubnetTest {

    @Test
    void getDescription() {
        Region region = Region.AP_NORTHEAST_2;
        try (Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build()) {
            DescribeSubnetsResponse describeSubnetsResponse = ec2.describeSubnets();
            describeSubnetsResponse.subnets().forEach(v -> {
                log.info("Subnet ID : " + v.subnetId());
                log.info("Subnet's VPC ID : " + v.vpcId());
            });
        } catch (Ec2Exception e) {
            e.printStackTrace();
        }
    }
}
