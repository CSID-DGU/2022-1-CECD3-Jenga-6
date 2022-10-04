package com.jenga.weather.domain.igw;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.List;

@Slf4j
public class IGWTest {

    @Test
    void getDescription() {

        Region region = Region.AP_NORTHEAST_2;
//        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("accessKey", "secretKey");

        try(Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build()) {

            DescribeInternetGatewaysResponse describeInternetGatewaysResponse = ec2.describeInternetGateways();
            List<InternetGateway> internetGateways = describeInternetGatewaysResponse.internetGateways();

            for (InternetGateway internetGateway : internetGateways) {
                log.info("internetGateway : {}", internetGateway);
            }
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
