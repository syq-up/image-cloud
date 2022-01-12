package com.shiyq.cloudsystem;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiyq.cloudsystem.entity.DO.Image;
import com.shiyq.cloudsystem.entity.DO.UserInfo;
import com.shiyq.cloudsystem.mapper.ImageMapper;
import com.shiyq.cloudsystem.mapper.UserInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

@SpringBootTest
class CloudSystemApplicationTests {

    @Value("${file.uploadFolder}")
    private String uploadFolder;

    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 更新`image`表，添加`size`字段，更新插入所有表项图像大小
     */
    @Test
    void updateImageForAddSize() {
        Image image = new Image();
        // 处理未逻辑删除的表项（图像）
        List<Image> imageList = imageMapper.selectList(new QueryWrapper<>());
        imageList.forEach(imageItem -> {
            // 找到图像文件
            String filePath = uploadFolder + String.format("%06d", imageItem.getUserId()) + "/" + imageItem.getPath();
            File file = new File(filePath);
            // 获取文件大小，更新表字段
            image.setId(imageItem.getId()).setSize((int) file.length());
            imageMapper.updateById(image);
        });
        // 处理 逻辑删除的表项（图像）
        List<Image> recycleList = imageMapper.getAllRecycleList();
        recycleList.forEach(imageItem -> {
            // 找到图像文件
            String filePath = uploadFolder + String.format("%06d", imageItem.getUserId()) + "/" + imageItem.getPath();
            File file = new File(filePath);
            // 获取文件大小，更新表字段
            image.setId(imageItem.getId()).setSize((int) file.length());
            imageMapper.updateById(image);
        });
        // 更新上传总大小统计数据
        long totalSize = 0L;
        for (Image imageItem : imageList) {
            totalSize += imageItem.getSize();
        }
        for (Image imageItem : recycleList) {
            totalSize += imageItem.getSize();
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setStoredSize(totalSize);
        userInfoMapper.updateById(userInfo);
    }

}
