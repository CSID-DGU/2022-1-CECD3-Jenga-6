package com.jenga.weather.domain.ec2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

@Slf4j
public class Ec2Test {

    @Test
    void getDescription() {
        String nextToken = null;

        Region region = Region.AP_NORTHEAST_2;
//        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("accessKey", "secretKey");

        try (Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();) {
            do {
                DescribeInstancesRequest request = DescribeInstancesRequest.builder().maxResults(6).nextToken(nextToken).build();
                DescribeInstancesResponse response = ec2.describeInstances(request);

                for (Reservation reservation : response.reservations()) {
                    for (Instance instance : reservation.instances()) {
                        log.info("Instance Id is : {}", instance.instanceId());
                        log.info("Image id is : {}", instance.imageId());
                        log.info("Instance type is : {}", instance.instanceType());
                        log.info("Instance state name is : {}", instance.state().name());
                        log.info("monitoring information is : {}", instance.monitoring().state());
                    }
                }
                nextToken = response.nextToken();
            } while (nextToken != null);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
