package org.example.demo.controller;

import org.example.demo.image.Image;
import org.example.demo.image.ImageDto;
import org.example.demo.member.CustomUserDetails;
import org.example.demo.member.Member;
import org.example.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/images/{regionUniqueId}")
    public List<ImageDto> getImagesByRegion(@PathVariable String regionUniqueId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();
        System.out.println("Received request for images in region: " + regionUniqueId);
        System.out.println("Member: " + (member != null ? member.getEmail() : "No member found"));

        List<Image> images = imageService.findImagesByRegionAndMember(regionUniqueId, member);

        images.forEach(image -> {
            System.out.println("Image found: " + image.getImageUrl());
        });

        return images.stream()
                .map(image -> {
                    ImageDto dto = new ImageDto();
                    dto.setImageId(image.getImageId());
                    dto.setRegionUniqueId(image.getRegionUniqueId());
                    dto.setImageUrl(image.getImageUrl());
                    dto.setUploadedAt(image.getUploadedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @PostMapping("/images/setPrimary")
    public ResponseEntity<Map<String, String>> setPrimaryImage(@RequestBody Map<String, Object> request) {
        Long imageId = ((Number) request.get("imageId")).longValue();
        String regionUniqueId = (String) request.get("regionUniqueId");

        System.out.println("Request received to set primary image: " + request);

        imageService.setPrimaryImage(imageId, regionUniqueId);


        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Primary image set successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/images/primary")
    public List<ImageDto> getPrimaryImages(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();
        System.out.println("Received request for primary images.");
        System.out.println("Member: " + (member != null ? member.getEmail() : "No member found"));

        List<Image> primaryImages = imageService.findPrimaryImagesByMember(member);

        primaryImages.forEach(image -> {
            System.out.println("Primary Image found: " + image.getImageUrl());
        });

        return primaryImages.stream()
                .map(image -> {
                    ImageDto dto = new ImageDto();
                    dto.setImageId(image.getImageId());
                    dto.setRegionUniqueId(image.getRegionUniqueId());
                    dto.setImageUrl(image.getImageUrl());
                    dto.setUploadedAt(image.getUploadedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }


}