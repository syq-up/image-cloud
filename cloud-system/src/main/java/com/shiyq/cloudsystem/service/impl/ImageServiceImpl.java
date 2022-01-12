package com.shiyq.cloudsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyq.cloudsystem.convert.ImageConvert;
import com.shiyq.cloudsystem.entity.DO.Image;
import com.shiyq.cloudsystem.entity.VO.ImageVO;
import com.shiyq.cloudsystem.entity.VO.PageVO;
import com.shiyq.cloudsystem.entity.DTO.UserContext;
import com.shiyq.cloudsystem.entity.VO.UploadRequest;
import com.shiyq.cloudsystem.mapper.ImageMapper;
import com.shiyq.cloudsystem.mapper.UserInfoMapper;
import com.shiyq.cloudsystem.service.ImageService;
import com.shiyq.cloudsystem.util.FileUtil;
import com.shiyq.cloudsystem.util.SnowFlakeUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Value("${file.staticAccessUrlPrefix}")
    private String imageUrlPrefix;

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
     * @param uploadRequest {图像存储的次级路径，网络图像链接}
     * @param file 图像
     * @return 图像对象 {id, path, ...}
     * @throws IOException 写文件异常
     */
    @Override
    @Transactional
    public ImageVO uploadImage(UploadRequest uploadRequest, MultipartFile file) throws IOException {
        // 用户上传路径
        String userUploadPath = uploadFolder + String.format("%06d", UserContext.getCurrentUserId()) + "/";
        String secondaryPath = uploadRequest.getSecondaryPath();
        // 新文件路径，则创建，并更新用户细信息（已创建次级路径列表）
        if (FileUtil.createNewPath(userUploadPath, secondaryPath)) {
            userInfoMapper.updateSecondaryPathByNewPath(UserContext.getCurrentUserId(), "\""+secondaryPath+"\"");
        }

        // 生成图像id，并作为新图像名
        long imageId = snowFlakeUtil.getNextId();
        // 写新文件，得到完整文件名
        String fullFilename = FileUtil.writeFile(userUploadPath + secondaryPath, String.valueOf(imageId), file);
        // 结合次级路径，得到图像存储名
        String fieldValue = uploadRequest.getSecondaryPath().length() == 0
                ? fullFilename
                : uploadRequest.getSecondaryPath() + "/" + fullFilename;

        // 创建新图像对象，插入数据库
        Image image = new Image(imageId, fieldValue, (int)file.getSize(), UserContext.getCurrentUserId());
        imageMapper.insert(image);
        // 更新用户总存储大小
        userInfoMapper.updateStoredSizeByIncrease(UserContext.getCurrentUserId(), file.getSize());

        // 拼接网站前缀组成完整的外部访问地址，再返回给前端
        return ImageConvert.INSTANCE.ImageDO2VO(
                image.setPath(imageUrlPrefix + String.format("%06d", UserContext.getCurrentUserId()) + "/" + fieldValue));
    }

    /**
     * 【多张图像】 上传文件到服务器并写入数据库
     * @param files 图像数组
     * @return 图像对象列表 [{id, path, ...}]
     * @throws IOException 写文件异常
     */
    @Override
    public List<ImageVO> uploadImages(UploadRequest uploadRequest, MultipartFile[] files) throws IOException {
        // 用户上传路径
        String userUploadPath = uploadFolder + String.format("%06d", UserContext.getCurrentUserId())
                + "/" + uploadRequest.getSecondaryPath();
        // 创建路径
        File userUploadPathFile = new File(userUploadPath);
        if(!userUploadPathFile.exists()) {
            if (!userUploadPathFile.mkdirs()) throw new IOException("创建文件夹失败！");
        }

        String suffix; // 文件后缀名
        long uploadFileName;   // 文件名（通过雪花算法重命名的文件名）
        String fullUploadFileName;
        List<ImageVO> imageList = new ArrayList<>();  // 返回的图像列表
        Image image = new Image();  // 图像

        // 逐一写入新文件，并插入数据库
        for (MultipartFile file : files){
            // 获取文件后缀名(eg: ".jpg")
            suffix = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            // 文件重命名
            uploadFileName = snowFlakeUtil.getNextId();
            // 存储到数据库的路径
            fullUploadFileName = uploadRequest.getSecondaryPath().length() == 0
                    ? uploadFileName + suffix
                    : uploadRequest.getSecondaryPath() + "/" + uploadFileName + suffix;
            // 写文件
            file.transferTo(new File(userUploadPath + "/" + uploadFileName + suffix));
            image.setId(uploadFileName).setPath(fullUploadFileName).setUserId(UserContext.getCurrentUserId());
            // 插入数据库和返回列表
            userInfoMapper.updateStoredSizeByIncrease(UserContext.getCurrentUserId(), file.getSize());
            imageMapper.insert(image);
            imageList.add(ImageConvert.INSTANCE.ImageDO2VO(
                    image.setPath(imageUrlPrefix + String.format("%06d", UserContext.getCurrentUserId())
                            + "/" + image.getPath())
            ));
        }
        return imageList;
    }

    /**
     * 【网络图像】上传文件到服务器并写入数据库
     * @param uploadRequest {图像存储的次级路径，网络图像链接}
     * @return 图像对象 {id, path, createTime}
     * @throws IOException 写文件异常
     */
    @Override
    @Transactional
    public ImageVO uploadWebImage(UploadRequest uploadRequest) throws IOException {
        // TODO 将网络图像大小也返回给前端？
        // 用户上传路径
        String userUploadPath = uploadFolder + String.format("%06d", UserContext.getCurrentUserId()) + "/";
        String secondaryPath = uploadRequest.getSecondaryPath();
        // 新文件路径，则创建，并更新用户细信息（已创建次级路径列表）
        if (FileUtil.createNewPath(userUploadPath, secondaryPath)) {
            userInfoMapper.updateSecondaryPathByNewPath(UserContext.getCurrentUserId(), "\""+secondaryPath+"\"");
        }

        // 获取文件后缀名(eg: ".jpg")
        String suffix = uploadRequest.getWebImageUrl().substring(uploadRequest.getWebImageUrl().lastIndexOf("."));
        // 生成图像id，并作为新图像名
        long imageId = snowFlakeUtil.getNextId();
        // 结合次级路径，得到图像存储名
        String fieldValue = uploadRequest.getSecondaryPath().length() == 0
                ? imageId + suffix
                : uploadRequest.getSecondaryPath() + "/" + imageId + suffix;
        // 写新文件
        File uploadFile = new File(userUploadPath + "/" + fieldValue);
        FileUtils.copyURLToFile(new URL(uploadRequest.getWebImageUrl()), uploadFile);

        // 创建新图像对象，插入数据库
        Image image = new Image(imageId, fieldValue, (int)uploadFile.length(), UserContext.getCurrentUserId());
        imageMapper.insert(image);
        // 更新用户总存储大小
        userInfoMapper.updateStoredSizeByIncrease(UserContext.getCurrentUserId(), uploadFile.length());

        // 拼接网站前缀组成完整的外部访问地址，再返回给前端
        return ImageConvert.INSTANCE.ImageDO2VO(
                image.setPath(imageUrlPrefix + String.format("%06d", UserContext.getCurrentUserId()) + "/" + fieldValue));
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
        page.getRecords().forEach(
                image -> image.setPath(imageUrlPrefix.replace("**", "") +
                        String.format("%06d", UserContext.getCurrentUserId()) + "/" + image.getPath())
        );

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
        recycleList.forEach(
                image -> image.setPath(imageUrlPrefix.replace("**", "") +
                        String.format("%06d", UserContext.getCurrentUserId()) + "/" + image.getPath())
        );
        pageVO.setRecords(ImageConvert.INSTANCE.ImageDOs2VOs(recycleList));
        // 回收站记录总数（逻辑删除的记录总数）
        pageVO.setTotal(imageMapper.getTotalRecycle(UserContext.getCurrentUserId()));
        return pageVO;
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

    // TODO 定时任务，物理删除图像


}
