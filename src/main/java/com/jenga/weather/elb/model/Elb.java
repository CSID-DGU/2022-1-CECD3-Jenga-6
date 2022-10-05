package com.jenga.weather.elb.model;

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
public class Elb {

    @Id
    private Long id;
    // 인스턴스 이름
    private String resourceName;
    // 인스턴스 상태
    private String state;
    // VPC
    private String vpcId;
    // 생성일자
    private LocalDateTime createdTime;


}
