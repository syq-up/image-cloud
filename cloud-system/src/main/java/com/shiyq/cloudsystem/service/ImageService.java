package com.shiyq.cloudsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyq.cloudsystem.entity.DO.Image;
import com.shiyq.cloudsystem.entity.VO.ImageVO;
import com.shiyq.cloudsystem.entity.VO.PageVO;
import com.shiyq.cloudsystem.entity.VO.UploadRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shiyq
 * @since 2022-01-02
 */
public interface ImageService extends IService<Image> {
    /**
     * 【单张图像】上传文件到服务器并写入数据库
     * @param uploadRequest {用户id，图像存储的次级路径，网络图像链接}
     * @param file 图像
     * @return 图像对象 {id, path, createTime}
     * @throws IOException 写文件异常
     */
    ImageVO uploadImage(UploadRequest uploadRequest, MultipartFile file) throws IOException;

    /**
     * 【网络图像】上传文件到服务器并写入数据库
     * @param uploadRequest {用户id，图像存储的次级路径，网络图像链接}
     * @return 图像对象 {id, path, createTime}
     * @throws IOException 写文件异常
     */
    ImageVO uploadWebImage(UploadRequest uploadRequest) throws Exception;

    /**
     * 查询图像列表
     * @param path      条件之一，为null则忽略
     * @param pageNum   条件之一，页数
     * @param deleted   条件之一，查询（未）逻辑删除的记录（true是，false否）
     */
    PageVO getImageList(String path, int pageNum, boolean deleted);

    /**
     * 逻辑删除，自动填充更新时间字段
     */
    boolean deleteByIdWithFill(long id);

    /**
     * 【单张图像】【回收】
     * @param id id
     * @return 是否成功
     */
    boolean restoreById(long id);
}
