package org.example.demo.service;

import org.example.demo.image.Image;
import org.example.demo.image.ImageDto;
import org.example.demo.image.ImageRepository;
import org.example.demo.member.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;


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

    @Transactional
    public List<ImageDto> saveImages(MultipartFile[] files, String regionUniqueId, Member member) {
        List<ImageDto> uploadedImages = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                // 회원 번호를 기반으로 디렉토리 생성
                String memberDir = uploadDir + "/" + member.getId();
                Path memberPath = Paths.get(memberDir);
                if (!Files.exists(memberPath)) {
                    Files.createDirectories(memberPath);
                }

                // 파일 저장
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(memberDir, fileName);
                Files.copy(file.getInputStream(), filePath);

                // 이미지 정보 저장
                Image image = new Image();
                image.setMember(member);
                image.setRegionUniqueId(regionUniqueId);
                image.setImageUrl("/uploads/" + member.getId() + "/" + fileName); // 상대 경로로 저장
                image.setUploadedAt(new Timestamp(System.currentTimeMillis()));
//                image.setIsPrimary(false);
                imageRepository.save(image);

                // DTO로 변환하여 리스트에 추가
                ImageDto dto = new ImageDto();
                dto.setImageId(image.getImageId());
                dto.setRegionUniqueId(image.getRegionUniqueId());
                dto.setImageUrl(image.getImageUrl());
                dto.setUploadedAt(image.getUploadedAt());
                uploadedImages.add(dto);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return uploadedImages;
    }


}