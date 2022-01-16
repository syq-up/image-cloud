package com.shiyq.cloudsystem.entity.DTO;

import lombok.Data;

@Data
public class ImageUploadDTO {
    private String filename;
    private long filesize;

    public ImageUploadDTO(String filename, long filesize) {
        this.filename = filename;
        this.filesize = filesize;
    }
}
