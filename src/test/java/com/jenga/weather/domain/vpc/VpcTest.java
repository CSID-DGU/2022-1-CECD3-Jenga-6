package com.jenga.weather.domain.vpc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;

@Slf4j
public class VpcTest {

    @Test
    void getDescription() {
        Region region = Region.AP_NORTHEAST_2;
        try (Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build()) {
            DescribeVpcsResponse describeVpcsResponse = ec2.describeVpcs();
            describeVpcsResponse.vpcs().forEach(vpc -> {
                log.info("VPC ID : " + vpc.vpcId());
                if (!vpc.tags().isEmpty())
                    log.info("VPC NAME : " + vpc.tags().get(0).value());
            });
        } catch (Ec2Exception e) {
            e.printStackTrace();
        }
    }
}



