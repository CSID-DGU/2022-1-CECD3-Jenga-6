package com.jenga.weather.domain.rds.service;

import com.jenga.weather.domain.rds.model.RDS;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.DescribeDbInstancesResponse;
import software.amazon.awssdk.services.rds.model.RdsException;
import software.amazon.awssdk.services.rds.model.Subnet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RDSService {

    public List<RDS> getRDSInstances() {
        List<RDS> rdsList = new ArrayList<>();

        Region region = Region.AP_NORTHEAST_2;
        try (RdsClient rdsClient = RdsClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build()) {
            DescribeDbInstancesResponse response = rdsClient.describeDBInstances();
            response.dbInstances().forEach(v -> {
                RDS rds = null;
                RDS.RDSBuilder rdsBuilder = RDS.builder()
                        .resourceId(v.dbiResourceId())
                        .resourceName(v.dbInstanceIdentifier())
                        .engine(v.engine())
                        .region(v.availabilityZone())
                        .dbInstanceClass(v.dbInstanceClass())
                        .dbInstanceStatus(v.dbInstanceStatus())
                        .subnetGroups(v.dbSubnetGroup().subnets())
                        .vpcId(v.dbSubnetGroup().vpcId());
                for (Subnet subnet : v.dbSubnetGroup().subnets()) {
                    if (Objects.equals(subnet.subnetAvailabilityZone().name(), v.availabilityZone()))
                        rds = rdsBuilder.subnetId(subnet.subnetIdentifier()).build();
                }

                rdsList.add(rds);
            });
        } catch (RdsException e) {
            System.err.println(e.awsErrorDetails().errorCode());
            System.exit(1);
        }
        return rdsList;
    }
}
