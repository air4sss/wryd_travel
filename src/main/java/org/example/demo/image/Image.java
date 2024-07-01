package org.example.demo.image;

import jakarta.persistence.*;
import lombok.Data;
import org.example.demo.member.Member;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String regionUniqueId;
    private String imageUrl;
    private Timestamp uploadedAt;

    @Column(name = "is_primary", columnDefinition = "TINYINT(1)")
    private Boolean isPrimary;
}