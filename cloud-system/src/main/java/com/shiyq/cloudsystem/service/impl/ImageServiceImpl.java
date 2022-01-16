package com.shiyq.cloudsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyq.cloudsystem.convert.ImageConvert;
import com.shiyq.cloudsystem.entity.DO.Image;
import com.shiyq.cloudsystem.entity.DTO.ImageUploadDTO;
import com.shiyq.cloudsystem.entity.VO.ImageVO;
import com.shiyq.cloudsystem.entity.VO.PageVO;
import com.shiyq.cloudsystem.entity.DTO.UserContext;
import com.shiyq.cloudsystem.entity.VO.UploadRequest;
import com.shiyq.cloudsystem.mapper.ImageMapper;
import com.shiyq.cloudsystem.mapper.UserInfoMapper;
import com.shiyq.cloudsystem.service.ImageService;
import com.shiyq.cloudsystem.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shiyq
 * @since 2022-01-02
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    @Value("${file.staticAccessUrlPrefix}")
    private String imageUrlPrefix;

    private ImageMapper imageMapper;
    private UserInfoMapper userInfoMapper;

    @Autowired
    public void setImageMapper(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    @Autowired
    public void setUserInfoMapper(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    /**
     * 【单张图像】 上传文件到服务器并写入数据库
     * @param uploadRequest {图像存储的次级路径，网络图像链接}
     * @param file 图像
     * @return 图像对象 {id, path, ...}
     * @throws IOException 写文件异常
     */
    @Override
    @Transactional
    public ImageVO uploadImage(UploadRequest uploadRequest, MultipartFile file) throws IOException {
        // 用户上传选择的次级路径
        String secondaryPath = uploadRequest.getSecondaryPath();
        // 新创建的次级路径，则创建，并更新用户信息（已创建次级路径列表）
        if (FileUploadUtil.INSTANCE.createNewPath(secondaryPath)) {
            userInfoMapper.updateSecondaryPathByIncrease(UserContext.getCurrentUserId(), "\""+secondaryPath+"\"");
        }

        // 写新文件，得到文件名
        String filename = FileUploadUtil.INSTANCE.uploadFile(secondaryPath, file);

        // 创建新图像对象，插入数据库，并更新用户总存储
        Image image = new Image(filename, secondaryPath, (int)file.getSize(), UserContext.getCurrentUserId());
        imageMapper.insert(image);
        userInfoMapper.updateStoredNumByIncrease(UserContext.getCurrentUserId(), 1);
        userInfoMapper.updateStoredSizeByIncrease(UserContext.getCurrentUserId(), file.getSize());

        // 拼接网站前缀组成完整的外部访问地址，再返回给前端
        String imageUrl = imageUrlPrefix + String.format("%06d", UserContext.getCurrentUserId()) + "/"
                + secondaryPath + "/" + filename;
        return ImageConvert.INSTANCE.ImageDO2VO(image.setPath(imageUrl));
    }

    /**
     * 【网络图像】上传文件到服务器并写入数据库
     * @param uploadRequest {图像存储的次级路径，网络图像链接}
     * @return 图像对象 {id, path, createTime}
     * @throws IOException 写文件异常
     */
    @Override
    @Transactional
    public ImageVO uploadWebImage(UploadRequest uploadRequest) throws Exception {
        // 用户上传选择的次级路径
        String secondaryPath = uploadRequest.getSecondaryPath();
        // 新文件路径，则创建，并更新用户细信息（已创建次级路径列表）
        if (FileUploadUtil.INSTANCE.createNewPath(secondaryPath)) {
            userInfoMapper.updateSecondaryPathByIncrease(UserContext.getCurrentUserId(), "\""+secondaryPath+"\"");
        }

        // 写新文件，得到文件名，文件大小
        ImageUploadDTO imageUploadDTO = FileUploadUtil.INSTANCE.uploadNetFile(
                secondaryPath, uploadRequest.getWebImageUrl());

        // 创建新图像对象，插入数据库
        Image image = new Image(imageUploadDTO.getFilename(), secondaryPath,
                (int)imageUploadDTO.getFilesize(), UserContext.getCurrentUserId());
        imageMapper.insert(image);
        // 更新用户总存储大小
        userInfoMapper.updateStoredNumByIncrease(UserContext.getCurrentUserId(), 1);
        userInfoMapper.updateStoredSizeByIncrease(UserContext.getCurrentUserId(), imageUploadDTO.getFilesize());

        // 拼接网站前缀组成完整的外部访问地址，再返回给前端
        String imageUrl = imageUrlPrefix + String.format("%06d", UserContext.getCurrentUserId()) + "/"
                + secondaryPath + "/" + imageUploadDTO.getFilename();
        return ImageConvert.INSTANCE.ImageDO2VO(image.setPath(imageUrl));
    }

    /**
     * 查询【图像列表】 默认以时间倒序获取
     * @param pageNum 页数
     * @return 图像列表
     */
    @Override
    public PageVO getImageList(long pageNum) {
        Page<Image> page = new Page<>(pageNum, 25);
        // 排序-最新上传优先
        QueryWrapper<Image> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", UserContext.getCurrentUserId());
        queryWrapper.orderByDesc("id");
        imageMapper.selectPage(page, queryWrapper);

        // 添加图像url前缀
        page.getRecords().forEach(image -> {
            String imageUrl = imageUrlPrefix + String.format("%06d", UserContext.getCurrentUserId()) + "/"
                    + image.getPath() + "/" + image.getFilename();
            image.setPath(imageUrl);
        });

        return ImageConvert.INSTANCE.page2PageVO(page);
    }

    /**
     * 查询【回收站列表】 默认以时间倒序获取
     * @param pageNum 页数
     * @return 回收站列表
     */
    @Override
    public PageVO getRecycleList(long pageNum) {
        PageVO pageVO = new PageVO(25, pageNum);
        // 根据页数获取回收站列表
        List<Image> recycleList =
                imageMapper.getRecycleListByIdAndPageNum(UserContext.getCurrentUserId(), 25, (pageNum-1)*25);
        // 添加图像url前缀
        recycleList.forEach(image -> {
            String imageUrl = imageUrlPrefix + String.format("%06d", UserContext.getCurrentUserId()) + "/"
                    + image.getPath() + "/" + image.getFilename();
            image.setPath(imageUrl);
        });
        pageVO.setRecords(ImageConvert.INSTANCE.ImageDOs2VOs(recycleList));
        // 回收站记录总数（逻辑删除的记录总数）
        pageVO.setTotal(imageMapper.getTotalRecycle(UserContext.getCurrentUserId()));
        return pageVO;
    }

    /**
     * 逻辑删除，自动填充更新时间字段
     */
    @Override
    public boolean deleteByIdWithFill(long id) {
        return imageMapper.deleteByIdWithFill(new Image(id)) >0;
    }

    /**
     * 【单张图像】【回收】
     * @param id id
     * @return 是否成功
     */
    @Override
    public boolean restoreById(long id) {
        return imageMapper.restoreById(id);
    }


}
