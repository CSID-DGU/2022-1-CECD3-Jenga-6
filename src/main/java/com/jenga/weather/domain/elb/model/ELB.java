package com.jenga.weather.domain.elb.model;

import com.jenga.weather.domain.base.AWSEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ELB extends AWSEntity {

    @Id
    private Long id;

    private String state;

    @Override
    public String toString() {
        return "ELB{" +
                "resourceName='" + resourceName + '\'' +
                ", vpcId='" + vpcId + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
