package com.jenga.weather.domain.rds.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jenga.weather.domain.base.AWSEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.services.rds.model.Subnet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

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

    @JsonIgnore
    @Transient
    private List<Subnet> subnetGroups;

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
