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
     * 【多张图像】上传文件到服务器并写入数据库
     * @param files 图像数组
     * @return 图像对象列表 [{id, path, createTime}]
     * @throws IOException 写文件异常
     */
    List<ImageVO> uploadImages(UploadRequest uploadRequest, MultipartFile[] files) throws IOException;

    /**
     * 【网络图像】上传文件到服务器并写入数据库
     * @param uploadRequest {用户id，图像存储的次级路径，网络图像链接}
     * @return 图像对象 {id, path, createTime}
     * @throws IOException 写文件异常
     */
    ImageVO uploadWebImage(UploadRequest uploadRequest) throws IOException;

    /**
     * 查询【图像列表】 默认以时间倒序获取
     * @param pageNum 页数
     * @return 图像列表
     */
    PageVO getImageList(long pageNum);

    /**
     * 查询【回收站列表】 默认以时间倒序获取
     * @param pageNum 页数
     * @return 回收站列表
     */
    PageVO getRecycleList(long pageNum);

    /**
     * 【单张图像】【回收】
     * @param id id
     * @return 是否成功
     */
    boolean restoreById(long id);
}
