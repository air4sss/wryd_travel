package org.example.demo.controller;

import org.example.demo.member.Member;
import org.example.demo.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class RegisterController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/register_kakao")
    public String showRegisterForm(Model model, @AuthenticationPrincipal OAuth2User oauthUser) {
        Member member = new Member();
        Map<String, Object> kakaoAccount = (Map<String, Object>) oauthUser.getAttributes().get("kakao_account");
        if (kakaoAccount != null) {
            String email = (String) kakaoAccount.get("email");
            member.setEmail(email);
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                String nickname = (String) profile.get("nickname");
                member.setName(nickname);
            }
        }
        member.setPlatform("kakao");
        model.addAttribute("member", member);
        return "register_kakao";
    }

    @PostMapping("/register_kakao")
    public String register(Member member) {
        member.setPlatform("kakao");
        memberRepository.save(member);
        return "redirect:/main";
    }
}