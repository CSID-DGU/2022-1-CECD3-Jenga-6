package com.jenga.weather.rds.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rds {

    @Id
    private Long id;
    // 인스턴스 이름(DB 식별자)
    private String resourceId;
    // 엔진 종류
    private String engine;
    // 리전
    private String region;
    // 인스턴스 종류
    private String dbInstanceClass;
    // 인스턴스 상태
    private String dbInstanceStatus;
    // VPC
    private String vpcId;
    // 인스턴스 생성일자
    private LocalDateTime createdTime;
}
