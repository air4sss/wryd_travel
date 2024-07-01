package org.example.demo.service;

import org.example.demo.member.Member;
import org.example.demo.member.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        String email = null;
        if (kakaoAccount != null) {
            email = (String) kakaoAccount.get("email");
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("카카오 계정에서 이메일을 가져올 수 없습니다.");
        }

        // 사용자 이메일 확인
        System.out.println("카카오 이메일: " + email);

        // 사용자 존재 여부 확인
        Optional<Member> existingMember = memberRepository.findByEmail(email);
        Member member;
        if (existingMember.isPresent()) {
            member = existingMember.get();
        } else {
            member = new Member();
            member.setEmail(email);
            member.setName((String) attributes.get("nickname")); // 예시로 닉네임을 설정, 필요시 수정
            member.setPlatform("kakao");
            memberRepository.save(member);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "id"
        );
    }
}