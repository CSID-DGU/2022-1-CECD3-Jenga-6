package com.jenga.weather.web.sign.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupForm {

    @NotEmpty(message = "아이디를 입력해주세요.")
    private String username;
    @NotEmpty(message = "비빌번호를 입력해주세요.")
    private String password;
    @NotEmpty(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm;
    @Email
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
}
