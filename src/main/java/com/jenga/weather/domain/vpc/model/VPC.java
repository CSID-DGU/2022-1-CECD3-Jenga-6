package com.jenga.weather.domain.vpc.model;

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
public class VPC extends AWSEntity {

    @Id
    private Long id;

    @Override
    public String toString() {
        return "VPC{" +
                "resourceId='" + resourceId + '\'' +
                ", resourceName='" + resourceName + '\'' +
                '}';
    }
}
