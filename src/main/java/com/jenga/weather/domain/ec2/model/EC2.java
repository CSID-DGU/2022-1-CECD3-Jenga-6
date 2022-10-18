package com.jenga.weather.domain.ec2.model;

import com.jenga.weather.domain.base.AWSEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EC2 extends AWSEntity {

    @Id
    private Long id;

    private String imageId;
    private String instanceType;
    private String keyName;
    private String privateDnsName;
    private String privateIpAddress;
    private String subnetId;

    @Override
    public String toString() {
        return "EC2{" +
                "resourceId='" + resourceId + '\'' +
                ", imageId='" + imageId + '\'' +
                ", vpcId='" + vpcId + '\'' +
                ", instanceType='" + instanceType + '\'' +
                ", keyName='" + keyName + '\'' +
                ", privateDnsName='" + privateDnsName + '\'' +
                ", privateIpAddress='" + privateIpAddress + '\'' +
                ", subnetId='" + subnetId + '\'' +
                '}';
    }
}
