package com.jenga.weather.domain.vpc;

import javax.persistence.Id;

public class Vpc {

    @Id
    private Long id;

    private String vpcId;
    private String name;
}
