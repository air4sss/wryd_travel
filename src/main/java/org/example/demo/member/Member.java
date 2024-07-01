package org.example.demo.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.demo.image.Image;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "member") // 테이블 이름을 명시적으로 지정
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String location;

    private String resetToken;

    private String platform;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
}