package com.jenga.weather.domain.rds;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.DBInstance;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesResponse;
import software.amazon.awssdk.services.rds.model.RdsException;

import java.util.List;

@Slf4j
public class RdsTest {

    @Test
    public void test() {
        Region region = Region.AP_NORTHEAST_2;

        try (RdsClient rdsClient = RdsClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build()) {

            describeInstances(rdsClient);
        } catch (RdsException e) {
            System.err.println(e.awsErrorDetails().errorCode());
            System.exit(1);
        }
    }

    public static void describeInstances(RdsClient rdsClient) {
        try {
            DescribeDbInstancesResponse response = rdsClient.describeDBInstances();
            List<DBInstance> instanceList = response.dbInstances();
            for (DBInstance instance : instanceList) {
                log.info("Instance ARN is: " + instance.dbInstanceArn());
                log.info("The Engine is " + instance.engine());
                log.info("Connection endpoint is" + instance.endpoint().address());
            }
        } catch (RdsException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }
}
