package com.jenga.weather.domain.nat;

import javax.persistence.Id;

public class Nat {

    @Id
    private Long id;

    private String vpcId;
    private String vpcName;
    private String subnetId;
}
