package org.example.demo.service;

import org.example.demo.member.CustomUserDetails;
import org.example.demo.member.Member;
import org.example.demo.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.trim().isEmpty()) {
            throw new UsernameNotFoundException("Email is empty");
        }

        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (!memberOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        Member member = memberOptional.get();
        System.out.println("Found member: " + member.getEmail());  // 멤버가 제대로 로드되는지 확인
//        return new org.springframework.security.core.userdetails.User(
//                member.getEmail(), member.getPassword(), new ArrayList<>()
//        );
//        return User.builder()
//                .username(member.getEmail())
//                .password(member.getPassword())
//                .roles("USER")
//                .build();
        return new CustomUserDetails(member);
    }
}