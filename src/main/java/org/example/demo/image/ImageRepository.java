package org.example.demo.image;

import org.example.demo.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByMember(Member member);
    List<Image> findByRegionUniqueIdAndMember(String regionUniqueId, Member member);
    List<Image> findByRegionUniqueId(String regionUniqueId);
    List<Image> findByMemberAndIsPrimaryTrue(Member member);
}