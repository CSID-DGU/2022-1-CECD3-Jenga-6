package com.jenga.weather.domain.nat.model;

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
public class NAT extends AWSEntity {

    @Id
    private Long id;

    private String subnetId;

    @Override
    public String toString() {
        return "NAT{" +
                "resourceId='" + resourceId + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", vpcId='" + vpcId + '\'' +
                ", subnetId='" + subnetId + '\'' +
                '}';
    }
}
