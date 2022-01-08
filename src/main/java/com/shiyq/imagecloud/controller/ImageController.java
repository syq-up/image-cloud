package com.shiyq.imagecloud.controller;

import com.shiyq.imagecloud.entity.DTO.XhrResult;
import com.shiyq.imagecloud.entity.VO.UploadDataVO;
import com.shiyq.imagecloud.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shiyq
 * @since 2022-01-02
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    private ImageService imageService;

    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * 【单张图像】上传
     * @param uploadDataVO {用户id，图像存储的次级路径，网络图像链接}
     * @param file 图像文件
     * @return 请求结果
     */
    @PostMapping("/upload")
    public XhrResult upload(UploadDataVO uploadDataVO, @RequestParam("file") MultipartFile file) {
        try {
            return XhrResult.success(imageService.uploadImage(uploadDataVO, file));
        } catch (IOException e) {
            return XhrResult.error("IO Exception");
        }
    }

    /**
     * 【网络图像】上传
     * @param uploadDataVO {图像存储的次级路径, 网络图像链接}
     * @return 请求结果
     */
    @PostMapping("/uploadWebImage")
    public XhrResult uploadWebImage(@RequestBody UploadDataVO uploadDataVO) {
        try {
            return XhrResult.success(imageService.uploadWebImage(uploadDataVO));
        } catch (IOException e) {
            return XhrResult.error("IO Exception");
        }
    }

    /**
     * 查询【图像列表】 默认以时间倒序获取
     * @param userId 用户id
     * @param pageNum 页数
     * @return 请求结果
     */
    @GetMapping("/getImageList/{userId}/{pageNum}")
    public XhrResult getImageListByPageNum(@PathVariable long userId, @PathVariable long pageNum) {
        return XhrResult.success(imageService.getImageList(userId, pageNum));
    }

    /**
     * 查询【回收站列表】 默认以时间倒序获取
     * @param userId 用户id
     * @param pageNum 页数
     * @return 请求结果
     */
    @GetMapping("/getRecycleList/{userId}/{pageNum}")
    public XhrResult getRecycleListByPageNum(@PathVariable long userId, @PathVariable long pageNum) {
        return XhrResult.success(imageService.getRecycleList(userId, pageNum));
    }

    /**
     * 【单张图像】【逻辑删除】
     * @param id id
     * @return 请求结果
     */
    @GetMapping("/deleteImageById/{id}")
    public XhrResult deleteImageById(@PathVariable long id) {
        return imageService.removeById(id) ? XhrResult.success("OK") : XhrResult.error("失败，请稍后尝试……");
    }

    /**
     * 【单张图像】【回收】
     * @param id id
     * @return 请求结果
     */
    @GetMapping("/restoreImageById/{id}")
    public XhrResult restoreImageById(@PathVariable long id) {
        return imageService.restoreById(id) ? XhrResult.success("OK") : XhrResult.error("失败，请稍后尝试……");
    }
}

