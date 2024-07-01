package org.example.demo.service;

import org.example.demo.image.Image;
import org.example.demo.image.ImageRepository;
import org.example.demo.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> findImagesByMember(Member member) {
        return imageRepository.findByMember(member);
    }

    public List<Image> findImagesByRegionAndMember(String regionUniqueId, Member member) {
        System.out.println("Finding images for region: " + regionUniqueId + " and member: " + (member != null ? member.getEmail() : "No member found"));
        List<Image> images = imageRepository.findByRegionUniqueIdAndMember(regionUniqueId, member);
        System.out.println("Number of images found: " + images.size());
        return imageRepository.findByRegionUniqueIdAndMember(regionUniqueId, member);
    }

    @Transactional
    public void setPrimaryImage(Long imageId, String regionUniqueId) {
        // 기존의 is_primary가 1인 이미지들 0으로 업데이트
        List<Image> images = imageRepository.findByRegionUniqueId(regionUniqueId);
        for (Image image : images) {
            if (image.getIsPrimary()) {
                image.setIsPrimary(false);
                imageRepository.save(image);
            }
        }

        // 새로운 이미지 is_primary 1로 업데이트
        Image newPrimaryImage = imageRepository.findById(imageId).orElseThrow(() -> new RuntimeException("Image not found"));
        newPrimaryImage.setIsPrimary(true);
        imageRepository.save(newPrimaryImage);
    }

    public List<Image> findPrimaryImagesByMember(Member member) {
        return imageRepository.findByMemberAndIsPrimaryTrue(member);
    }


}