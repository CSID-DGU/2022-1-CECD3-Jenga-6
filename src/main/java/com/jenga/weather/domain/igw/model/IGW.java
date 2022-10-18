package com.jenga.weather.domain.igw.model;

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
public class IGW extends AWSEntity {

    @Id
    private Long id;

    private String ownerId;
    private String vpcState;

    @Override
    public String toString() {
        return "IGW{" +
                "resourceId='" + resourceId + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", vpcId='" + vpcId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", vpcState='" + vpcState + '\'' +
                '}';
    }
}
