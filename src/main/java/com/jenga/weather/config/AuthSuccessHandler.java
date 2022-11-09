package com.jenga.weather.config;

import com.jenga.weather.domain.member.entity.Member;
import com.jenga.weather.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Member member = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        if (member.getAccessKey() == null || member.getSecretKey() == null) {
            setDefaultTargetUrl("/aws-key");
            super.onAuthenticationSuccess(request, response, authentication);
        }
        else {
            setDefaultTargetUrl("/infra/visualization");
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}