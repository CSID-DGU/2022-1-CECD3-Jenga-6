package com.jenga.weather.web.sign.controller;

import com.jenga.weather.domain.member.service.MemberService;
import com.jenga.weather.web.sign.dto.KeysForm;
import com.jenga.weather.web.sign.dto.LoginForm;
import com.jenga.weather.web.sign.dto.SignupForm;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class SignController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "page/member_signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute SignupForm signupForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "page/member_signup";
        }

        try {
            memberService.save(signupForm);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.rejectValue("username", "duplicated", "이미 등록된 아이디입니다.");
            return "page/member_signup";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "error", required = false) String error) {

        model.addAttribute("error", error);
        model.addAttribute("errorMsg", "로그인이 실패했습니다.");
        model.addAttribute("loginForm", new LoginForm());
        return "page/member_login";
    }

    @GetMapping("/aws-key")
    public String showKeysForm(Model model) {
        model.addAttribute("keysForm", new KeysForm());
        return "page/member_keys";
    }

    @PostMapping("/aws-key")
    public String saveKeys(@Validated @ModelAttribute KeysForm keysForm, BindingResult bindingResult,
                           Principal principal) {

        if (bindingResult.hasErrors()) {
            return "page/member_keys";
        }

        memberService.updateKeys(principal.getName(), keysForm.getAccessKey(), keysForm.getSecretKey());
        return "redirect:/infra/visualization";
    }
}
