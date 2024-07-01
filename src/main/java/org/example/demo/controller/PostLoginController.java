package org.example.demo.controller;

import org.example.demo.member.Member;
import org.example.demo.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.Optional;

@Controller
public class PostLoginController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/postLogin")
    public String postLogin(@AuthenticationPrincipal OAuth2User oauthUser) {
        Map<String, Object> attributes = oauthUser.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        String email = null;
        if (kakaoAccount != null) {
            email = (String) kakaoAccount.get("email");
        }

        Optional<Member> existingMember = memberRepository.findByEmail(email);
        if (existingMember.isPresent()) {
            return "redirect:/main";
        } else {
            return "redirect:/register_kakao";
        }
    }
}