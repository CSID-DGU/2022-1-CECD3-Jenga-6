package com.jenga.weather.domain.s3.model;

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
public class S3 extends AWSEntity {

    @Id
    private Long id;

    @Override
    public String toString() {
        return "S3{" +
                "resourceName='" + resourceName + '\'' +
                '}';
    }
}
