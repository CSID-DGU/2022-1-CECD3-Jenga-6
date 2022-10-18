package com.jenga.weather.domain.elb.service;

import com.jenga.weather.domain.elb.model.ELB;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.elasticloadbalancingv2.ElasticLoadBalancingV2Client;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.DescribeLoadBalancersResponse;
import software.amazon.awssdk.services.elasticloadbalancingv2.model.ElasticLoadBalancingV2Exception;

import java.util.ArrayList;
import java.util.List;

@Service
public class ELBService {
    public List<ELB> getELBInstances() {
        List<ELB> elbList = new ArrayList<>();

        Region region = Region.AP_NORTHEAST_2;
        try (ElasticLoadBalancingV2Client elbClient = ElasticLoadBalancingV2Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build()) {
            DescribeLoadBalancersResponse response = elbClient.describeLoadBalancers();
            response.loadBalancers().forEach(v -> {
                ELB elb = ELB.builder()
                        .resourceName(v.loadBalancerName())
                        .state(v.state().codeAsString())
                        .vpcId(v.vpcId())
                        .build();

                elbList.add(elb);
            });

        } catch (ElasticLoadBalancingV2Exception e) {
            System.err.println(e.awsErrorDetails().errorCode());
            System.exit(1);
        }
        return elbList;
    }
}

