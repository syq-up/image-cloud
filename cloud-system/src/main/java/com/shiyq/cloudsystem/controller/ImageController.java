package com.shiyq.cloudsystem.controller;

import com.shiyq.cloudsystem.entity.VO.XhrResult;
import com.shiyq.cloudsystem.entity.VO.UploadRequest;
import com.shiyq.cloudsystem.service.ImageService;
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
     * @param uploadRequest {图像存储的次级路径，网络图像链接}
     * @param file 图像文件
     * @return 请求结果
     */
    @PostMapping("/upload")
    public XhrResult upload(UploadRequest uploadRequest, @RequestParam("file") MultipartFile file) {
        try {
            return XhrResult.success(imageService.uploadImage(uploadRequest, file));
        } catch (IOException e) {
            return XhrResult.error("IO Exception");
        }
    }

    /**
     * 【网络图像】上传
     * @param uploadRequest {图像存储的次级路径, 网络图像链接}
     * @return 请求结果
     */
    @PostMapping("/uploadWebImage")
    public XhrResult uploadWebImage(@RequestBody UploadRequest uploadRequest) {
        try {
            return XhrResult.success(imageService.uploadWebImage(uploadRequest));
        } catch (IOException e) {
            return XhrResult.error("IO Exception");
        }
    }

    /**
     * 查询【图像列表】 默认以时间倒序获取
     * @param pageNum 页数
     * @return 请求结果
     */
    @GetMapping("/getImageList/{pageNum}")
    public XhrResult getImageListByPageNum(@PathVariable long pageNum) {
        return XhrResult.success(imageService.getImageList(pageNum));
    }

    /**
     * 查询【回收站列表】 默认以时间倒序获取
     * @param pageNum 页数
     * @return 请求结果
     */
    @GetMapping("/getRecycleList/{pageNum}")
    public XhrResult getRecycleListByPageNum(@PathVariable long pageNum) {
        return XhrResult.success(imageService.getRecycleList(pageNum));
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

