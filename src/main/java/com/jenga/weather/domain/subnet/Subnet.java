package com.jenga.weather.domain.subnet;

import javax.persistence.Id;

public class Subnet {

    @Id
    private Long id;

    private String name;
    private String vpcId;
    private String vpcName;
}
