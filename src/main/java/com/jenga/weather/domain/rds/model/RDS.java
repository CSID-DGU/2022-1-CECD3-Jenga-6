package com.jenga.weather.domain.rds.model;

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
public class RDS extends AWSEntity {

    @Id
    private Long id;

    private String engine;
    private String region;
    private String dbInstanceClass;
    private String dbInstanceStatus;
    private String subnetId;

    @Override
    public String toString() {
        return "RDS{" +
                "resourceId='" + resourceId + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", vpcId='" + vpcId + '\'' +
                ", engine='" + engine + '\'' +
                ", region='" + region + '\'' +
                ", dbInstanceClass='" + dbInstanceClass + '\'' +
                ", dbInstanceStatus='" + dbInstanceStatus + '\'' +
                ", subnetId='" + subnetId + '\'' +
                '}';
    }
}
