package com.jenga.weather.domain.igw;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class IGW {

    @Id
    private Long id;

    private String internetGatewayId;
    private String name;
    private String ownerId;

    private String vpcId;
    private String vpcState;
}
