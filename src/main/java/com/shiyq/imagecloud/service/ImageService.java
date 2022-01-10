package com.shiyq.imagecloud.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyq.imagecloud.entity.DO.Image;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyq.imagecloud.entity.DTO.ImageDTO;
import com.shiyq.imagecloud.entity.DTO.PageDTO;
import com.shiyq.imagecloud.entity.VO.UploadDataVO;
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
     * @param uploadDataVO {用户id，图像存储的次级路径，网络图像链接}
     * @param file 图像
     * @return 图像对象 {id, path, createTime}
     * @throws IOException 写文件异常
     */
    ImageDTO uploadImage(UploadDataVO uploadDataVO, MultipartFile file) throws IOException;

    /**
     * 【多张图像】上传文件到服务器并写入数据库
     * @param files 图像数组
     * @return 图像对象列表 [{id, path, createTime}]
     * @throws IOException 写文件异常
     */
    List<ImageDTO> uploadImages(UploadDataVO uploadDataVO, MultipartFile[] files) throws IOException;

    /**
     * 【网络图像】上传文件到服务器并写入数据库
     * @param uploadDataVO {用户id，图像存储的次级路径，网络图像链接}
     * @return 图像对象 {id, path, createTime}
     * @throws IOException 写文件异常
     */
    ImageDTO uploadWebImage(UploadDataVO uploadDataVO) throws IOException;

    /**
     * 查询【图像列表】 默认以时间倒序获取
     * @param pageNum 页数
     * @return 图像列表
     */
    PageDTO getImageList(long pageNum);

    /**
     * 查询【回收站列表】 默认以时间倒序获取
     * @param pageNum 页数
     * @return 回收站列表
     */
    PageDTO getRecycleList(long pageNum);

    /**
     * 【单张图像】【回收】
     * @param id id
     * @return 是否成功
     */
    boolean restoreById(long id);
}
