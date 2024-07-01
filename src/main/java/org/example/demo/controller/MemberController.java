package org.example.demo.controller;

import org.example.demo.member.Member;
import org.example.demo.member.MemberRepository;
import org.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/register")
    public String registerMember(@ModelAttribute Member member, Model model) {
        try {
            // 비밀번호 인코딩
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            memberRepository.save(member);
            return "redirect:/register?success";
        } catch (DataIntegrityViolationException e) {
            // 중복된 아이디 입력 시
            model.addAttribute("member", member);
            model.addAttribute("errorMessage", "이미 등록된 이메일입니다. 다른 이메일을 사용해주세요.");
            return "register";
        }
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String userEmail, Model model) {
        System.out.println("userEmail : "+userEmail);
        // 여기서 사용자 이메일을 확인하고 임시 비밀번호 또는 비밀번호 재설정 링크를 생성.
        String resetLink = "http://your-app.com/reset-password?token=exampleToken";
        emailService.sendSimpleMail(userEmail, "Password Reset Request",
                "To reset your password, click the link below:\n" + resetLink);

        model.addAttribute("message", "Password reset link sent to your email.");
        return "forgot";
    }
}