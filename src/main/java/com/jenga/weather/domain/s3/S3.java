package com.jenga.weather.domain.s3;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class S3 {

    @Id
    private Long id;
    private String name;
    private LocalDateTime creationDate;
}
