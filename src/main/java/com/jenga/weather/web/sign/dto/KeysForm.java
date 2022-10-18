package com.jenga.weather.web.sign.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class KeysForm {

    @NotEmpty(message = "엑세스 키를 입력해주세요.")
    private String accessKey;
    @NotEmpty(message = "시크릿 키를 입력해주세요.")
    private String secretKey;
}
