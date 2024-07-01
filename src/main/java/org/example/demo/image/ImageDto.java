package org.example.demo.image;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ImageDto {
    private Long imageId;
    private String regionUniqueId;
    private String imageUrl;
    private Timestamp uploadedAt;

}
