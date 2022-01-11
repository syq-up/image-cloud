package com.shiyq.cloudsystem.entity.VO;

import lombok.Data;
import java.util.List;

/**
 * 返回前端，图像列表信息
 */
@Data
public class PageVO {
    private List<ImageVO> records;
    private long total;
    private long size;
    private long current;

    public PageVO() {
    }

    public PageVO(long size, long current) {
        this.size = size;
        this.current = current;
    }
}
