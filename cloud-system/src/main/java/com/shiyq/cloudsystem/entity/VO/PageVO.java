package com.shiyq.cloudsystem.entity.VO;

import lombok.Data;
import java.util.List;

/**
 * 返回前端，图像列表信息
 */
@Data
public class PageVO {
    private List<ImageVO> records;  // 当前页所有记录
    private long total; // 总记录数
    private long size;  // 页大小
    private long current;   // 当前页数

    public PageVO() {
    }

    public PageVO(long size, long current) {
        this.size = size;
        this.current = current;
    }
}
