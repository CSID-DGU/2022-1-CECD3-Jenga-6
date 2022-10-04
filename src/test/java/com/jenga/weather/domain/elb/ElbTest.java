package com.jenga.weather.domain.elb;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.elasticloadbalancing.model.LoadBalancerNotFoundException;
import software.amazon.awssdk.services.elasticloadbalancingv2.ElasticLoadBalancingV2Client;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.DescribeLoadBalancersResponse;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.LoadBalancer;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
public class ElbTest {
    @Test
    public void test() {
        Region region = Region.AP_NORTHEAST_2;

        try (ElasticLoadBalancingV2Client elb = ElasticLoadBalancingV2Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build()) {

            describeElbInstances(elb);
        }
    }

    public static void describeElbInstances(ElasticLoadBalancingV2Client test) {
        String nextToken = null;
        try {
            DescribeLoadBalancersResponse response = test.describeLoadBalancers();
            List<LoadBalancer> loadBalancers = response.loadBalancers();
            for (LoadBalancer instance : loadBalancers) {
                log.info("Instance Id is " + instance.loadBalancerName());
                log.info("Image id is " + instance.state().code());
                log.info("Instance type is " + instance.vpcId());
                log.info("Instance state name is " + instance.createdTime());
            }
        } catch (LoadBalancerNotFoundException e) {
            System.err.println(e.awsErrorDetails().errorCode());
            System.exit(1);
        }
    }
}