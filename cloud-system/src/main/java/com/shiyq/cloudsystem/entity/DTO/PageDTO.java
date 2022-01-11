package com.shiyq.cloudsystem.entity.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PageDTO {
    private List<ImageDTO> records;
    private long total;
    private long size;
    private long current;

    public PageDTO() {
    }

    public PageDTO(long size, long current) {
        this.size = size;
        this.current = current;
    }
}
