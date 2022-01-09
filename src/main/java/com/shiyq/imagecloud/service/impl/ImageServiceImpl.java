package com.shiyq.imagecloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyq.imagecloud.convert.ImageConvert;
import com.shiyq.imagecloud.entity.DO.Image;
import com.shiyq.imagecloud.entity.DTO.ImageDTO;
import com.shiyq.imagecloud.entity.DTO.PageDTO;
import com.shiyq.imagecloud.entity.VO.UploadDataVO;
import com.shiyq.imagecloud.mapper.ImageMapper;
import com.shiyq.imagecloud.mapper.UserInfoMapper;
import com.shiyq.imagecloud.service.ImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyq.imagecloud.util.SnowFlakeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Value("${file.uploadFolder}")
    private String uploadFolder;
    @Value("${url}${server.servlet.context-path}${file.staticAccessPath}")
    private String imageUrlPrefix;
    @Value("${pageSize}")
    private long pageSize;

    private ImageMapper imageMapper;
    private UserInfoMapper userInfoMapper;
    private SnowFlakeUtil snowFlakeUtil;

    @Autowired
    public void setImageMapper(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    @Autowired
    public void setUserInfoMapper(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Autowired
    public void setSnowFlakeUtil(SnowFlakeUtil snowFlakeUtil) {
        this.snowFlakeUtil = snowFlakeUtil;
    }

    /**
     * 【单张图像】 上传文件到服务器并写入数据库
     * @param uploadDataVO {用户id，图像存储的次级路径，网络图像链接}
     * @param file 图像
     * @return 图像对象列表 {id, path, ...}
     * @throws IOException 写文件异常
     */
    @Override
    public ImageDTO uploadImage(UploadDataVO uploadDataVO, MultipartFile file) throws IOException {
        // TODO 提出文件上传工具方法
        // TODO 记录次级路径
        // 用户上传路径
        String userUploadPath =
                uploadFolder + String.format("%06d", uploadDataVO.getUserId()) + "/" + uploadDataVO.getSecondaryPath();
        // 创建路径
        createDirIfNotExists(userUploadPath);
        // 获取文件后缀名(eg: ".jpg")
        String suffix = Objects.requireNonNull(
                file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        // 文件重命名
        long uploadFileName = snowFlakeUtil.getNextId();
        // 存储到数据库的路径
        String fullUploadFileName = uploadDataVO.getSecondaryPath().length() == 0
                ? uploadFileName + suffix
                : uploadDataVO.getSecondaryPath() + "/" + uploadFileName + suffix;
        // 写文件
        file.transferTo(new File(userUploadPath + "/" + uploadFileName + suffix));
        Image image = new Image(uploadFileName, fullUploadFileName, uploadDataVO.getUserId());
        // 插入数据库
        userInfoMapper.updateStoredSizeByIncrease(uploadDataVO.getUserId(), file.getSize());
        imageMapper.insert(image);

        return ImageConvert.INSTANCE.ImageDOToDTO(
                image.setPath(imageUrlPrefix.replace("**", "") +
                        String.format("%06d", uploadDataVO.getUserId()) + "/" + image.getPath())
        );
    }

    /**
     * 【多张图像】 上传文件到服务器并写入数据库
     * @param files 图像数组
     * @return 图像对象列表 [{id, path, ...}]
     * @throws IOException 写文件异常
     */
    @Override
    public List<ImageDTO> uploadImages(UploadDataVO uploadDataVO, MultipartFile[] files) throws IOException {
        // 用户上传路径
        String userUploadPath =
                uploadFolder + String.format("%06d", uploadDataVO.getUserId()) + "/" + uploadDataVO.getSecondaryPath();
        // 创建路径
        createDirIfNotExists(userUploadPath);

        String suffix; // 文件后缀名
        long uploadFileName;   // 文件名（通过雪花算法重命名的文件名）
        String fullUploadFileName;
        List<ImageDTO> imageList = new ArrayList<>();  // 返回的图像列表
        Image image = new Image();  // 图像

        // 逐一写入新文件，并插入数据库
        for (MultipartFile file : files){
            // 获取文件后缀名(eg: ".jpg")
            suffix = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            // 文件重命名
            uploadFileName = snowFlakeUtil.getNextId();
            // 存储到数据库的路径
            fullUploadFileName = uploadDataVO.getSecondaryPath().length() == 0
                    ? uploadFileName + suffix
                    : uploadDataVO.getSecondaryPath() + "/" + uploadFileName + suffix;
            // 写文件
            file.transferTo(new File(userUploadPath + "/" + uploadFileName + suffix));
            image.setId(uploadFileName).setPath(fullUploadFileName).setUserId(uploadDataVO.getUserId());
            // 插入数据库和返回列表
            userInfoMapper.updateStoredSizeByIncrease(uploadDataVO.getUserId(), file.getSize());
            imageMapper.insert(image);
            imageList.add(ImageConvert.INSTANCE.ImageDOToDTO(
                    image.setPath(imageUrlPrefix.replace("**", "") +
                            String.format("%06d", uploadDataVO.getUserId()) + "/" + image.getPath())
            ));
        }
        return imageList;
    }

    /**
     * 【网络图像】上传文件到服务器并写入数据库
     * @param uploadDataVO {用户id，图像存储的次级路径，网络图像链接}
     * @return 图像对象 {id, path, createTime}
     * @throws IOException 写文件异常
     */
    @Override
    public ImageDTO uploadWebImage(UploadDataVO uploadDataVO) throws IOException {
        // TODO 将网络图像大小也返回给前端？
        // 用户上传路径
        String userUploadPath =
                uploadFolder + String.format("%06d", uploadDataVO.getUserId()) + "/" + uploadDataVO.getSecondaryPath();
        // 创建路径
        createDirIfNotExists(userUploadPath);
        // 创建根路径
        File uploadPath = new File(uploadFolder +
                String.format("%06d", uploadDataVO.getUserId()) + uploadDataVO.getSecondaryPath());
        if (!uploadPath.exists()) uploadPath.mkdirs();
        // 获取文件后缀名(eg: ".jpg")
        String suffix = FilenameUtils.getName(uploadDataVO.getWebImageUrl())
                .substring(FilenameUtils.getName(uploadDataVO.getWebImageUrl()).lastIndexOf("."));
        // 文件重命名
        long uploadFileName = snowFlakeUtil.getNextId();
        // 存储到数据库的路径
        String fullUploadFileName = uploadDataVO.getSecondaryPath().length() == 0
                ? uploadFileName + suffix
                : uploadDataVO.getSecondaryPath() + "/" + uploadFileName + suffix;
        // 写文件
        File uploadFile = new File(userUploadPath + "/" + uploadFileName + suffix);
        FileUtils.copyURLToFile(new URL(uploadDataVO.getWebImageUrl()), uploadFile);

        Image image = new Image(uploadFileName, fullUploadFileName, uploadDataVO.getUserId());
        // 插入数据库
        userInfoMapper.updateStoredSizeByIncrease(uploadDataVO.getUserId(), uploadFile.length());
        imageMapper.insert(image);

        return ImageConvert.INSTANCE.ImageDOToDTO(
                image.setPath(imageUrlPrefix.replace("**", "") +
                        String.format("%06d", uploadDataVO.getUserId()) + "/" + image.getPath())
        );
    }

    /**
     * 查询【图像列表】 默认以时间倒序获取
     * @param userId 用户id
     * @param pageNum 页数
     * @return 图像列表
     */
    @Override
    public PageDTO getImageList(long userId, long pageNum) {
        Page<Image> page = new Page<>(pageNum, pageSize);
        // 排序-最新上传优先
        QueryWrapper<Image> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("id");
        imageMapper.selectPage(page, queryWrapper);

        // 添加图像url前缀
        page.getRecords().forEach(
                image -> image.setPath(imageUrlPrefix.replace("**", "") +
                        String.format("%06d", userId) + "/" + image.getPath())
        );

        return ImageConvert.INSTANCE.PageToPageDTO(page);
    }

    /**
     * 查询【回收站列表】 默认以时间倒序获取
     * @param userId 用户id
     * @param pageNum 页数
     * @return 回收站列表
     */
    @Override
    public PageDTO getRecycleList(long userId, long pageNum) {
        PageDTO pageDTO = new PageDTO(pageSize, pageNum);
        // 查回收站记录（逻辑删除的记录）
        String sql =
                "SELECT * FROM `image` " +
                "WHERE `deleted` = 1 AND `user_id` = " + userId + " " +
                "ORDER BY `create_time` DESC " +
                "LIMIT "+pageSize+" OFFSET "+(pageNum-1)*pageSize+";";
        List<Image> recycleList = imageMapper.getRecycleListBySql(sql);
        // 添加图像url前缀
        recycleList.forEach(
                image -> image.setPath(imageUrlPrefix.replace("**", "") +
                        String.format("%06d", userId) + "/" + image.getPath())
        );
        pageDTO.setRecords(ImageConvert.INSTANCE.ImageDOsToDTOs(recycleList));
        // 查回收站记录总数（逻辑删除的记录总数）
        pageDTO.setTotal(imageMapper.getTotalRecycle());
        return pageDTO;
    }

    /**
     * 【单张图像】【回收】
     * @param id id
     * @return 是否成功
     */
    @Override
    public boolean restoreById(long id) {
        String sql = "UPDATE `image`  SET `deleted` = 0  WHERE `id` = "+ id +";";
        return imageMapper.restoreBySql(sql) > 0;
    }

    // TODO 定时任务，物理删除图像


    /**
     * 创建文件夹（如果文件夹不存在）
     * @param path 需要创建的文件夹路径
     */
    public void createDirIfNotExists(String path) {
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }
    }

}
