package com.shiyq.cloudsystem.config;

import com.shiyq.cloudsystem.entity.DO.Image;
import com.shiyq.cloudsystem.mapper.ImageMapper;
import com.shiyq.cloudsystem.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class TimerConfig {

    // 已逻辑删除的图像，最长保留时间（10天）
    private static final long MAX_STAY_TIME = 10*24*60*60*1000;

    // 图像上传根路径
    @Value("${file.uploadFolder}")
    private String uploadFolder;

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
     * 每天03：00执行，对逻辑时间达到最大保留时间的图像进行删除
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteExpiredImages() {
        // 图像应当被删除的极限时间
        long limitDate = new Date().getTime() - MAX_STAY_TIME;

        List<Image> imageList = imageMapper.getAllRecycleList();
        // 对逻辑删除时间大于等于limitDate的图像执行删除
        imageList.stream().filter(image -> image.getUpdateTime().getTime() <= limitDate).forEach(image -> {
            if (imageMapper.realDeleteById(image.getId())) {
                String filePath = uploadFolder + String.format("%06d", image.getUserId()) + "/" + image.getPath();
                if (!new File(filePath).delete()) {
                    System.out.println("定时任务-物理删除-失败：图像地址=" + filePath);
                }
                // 更新用户的总存储大小
                userInfoMapper.updateStoredSizeByIncrease(image.getUserId(), image.getSize()*(-1L));
            } else {
                System.out.println("定时任务-物理删除-失败：图像id=" + image.getId());
            }
        });
    }

}
