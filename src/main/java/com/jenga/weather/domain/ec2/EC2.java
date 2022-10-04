package com.jenga.weather.domain.ec2;

import javax.persistence.Id;

public class EC2 {

    @Id
    private Long id;

    private String imageId;
    private String instanceId;
    private String instanceType;

    private String keyName;
    private String privateDnsName;
    private String privateIpAddress;

    private String subnetId;
    private String vpcId;
}
