package com.shiyq.cloudsystem.util;

import com.shiyq.cloudsystem.entity.DTO.ImageUploadDTO;
import com.shiyq.cloudsystem.entity.DTO.UserContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Component
public class FileUploadUtil {
    public static FileUploadUtil INSTANCE;

    @Value("${file.uploadFolder}")
    private String uploadFolder;
    @Value("${file.maxFileSize}")
    private String maxFileSize;

    private SnowFlakeUtil snowFlakeUtil;

    @Autowired
    public void setSnowFlakeUtil(SnowFlakeUtil snowFlakeUtil) {
        this.snowFlakeUtil = snowFlakeUtil;
    }

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    /**
     * 指定次级路径，文件对象，写入新文件；并返回文件名
     * @param secondaryPath 指定次级路径
     * @param file 指定文件对象
     * @return 文件名
     */
    public String uploadFile(String secondaryPath, MultipartFile file) throws IOException {
        // 用户上传路径
        String userUploadPath = uploadFolder + String.format("%06d", UserContext.getCurrentUserId()) + "/";
        // 文件重命名
        String filename = snowFlakeUtil.getNextId() + "." + getExtension(file);
        // 写新文件
        try {
            file.transferTo(new File(userUploadPath + secondaryPath + "/" + filename));
        } catch (IOException e) {
            throw new IOException(e.getMessage(), e);
        }
        return filename;
    }

    /**
     * 指定次级路径，网络文件地址，写入新文件；并返回文件名
     * @param secondaryPath 指定次级路径
     * @param fileUrl 指定网络文件地址
     * @return DTO{文件名，文件大小}
     */
    public ImageUploadDTO uploadNetFile(String secondaryPath, String fileUrl) throws Exception {
        // 用户上传路径
        String userUploadPath = uploadFolder + String.format("%06d", UserContext.getCurrentUserId()) + "/";
        // 文件重命名
        String filename = snowFlakeUtil.getNextId() + "." + getExtension(fileUrl);
        // 写新文件
        File newFile = new File(userUploadPath + "/" + filename);
        try {
            FileUtils.copyURLToFile(new URL(fileUrl), newFile);
        } catch (IOException e) {
            throw new IOException("复制网络文件【"+fileUrl+"】发生错误！");
        }
        if (newFile.length() >= sizeStringToLong(maxFileSize)) {
            if (!newFile.delete()) {
                System.out.println("文件删除失败--上传的网络文件过大--"+newFile.getPath());
            }
            throw new Exception("单个文件不得超过"+maxFileSize+"!");
        }
        return new ImageUploadDTO(filename, newFile.length());
    }

    /**
     * 创建新的次级路径
     * @return 路径是否已存在
     */
    public boolean createNewPath(String secondaryPath) throws IOException {
        // 用户上传路径
        String userPath = uploadFolder + String.format("%06d", UserContext.getCurrentUserId());
        // 新创建用户路径时不返回
        File userPathFile = new File(userPath);
        if(!userPathFile.exists()) {
            if (!userPathFile.mkdirs())
                throw new IOException("创建文件夹【"+userPathFile+"】发生错误！");
        }
        // 新创建子路径时返回true，否则返回false
        File newPathFile = new File(userPath + "/" + secondaryPath);
        if(!newPathFile.exists()) {
            if (!newPathFile.mkdirs())
                throw new IOException("创建文件夹【"+newPathFile+"】发生错误！");
            return true;
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     */
    public static String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension == null || extension.length() == 0) {
            extension = MimeTypeUtil.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }

    /**
     * 获取网络文件后缀名
     */
    public static String getExtension(String imageUrl) throws Exception {
        String extension;
        try {
            extension = MimeTypeUtil.getFileTypeByNetPath(imageUrl);
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
        // 没有检测到的类型按jpg处理
        return extension != null ? extension : ".jpg";
    }

    /**
     * 此方法并不健壮！谨慎使用！！！
     * 仅支持规范型输入，eg.”xxxMB“、”xxxKB“
     * 字符串类型文件大小转long型，eg.”5MB“-->5*1024*1024
     */
    private long sizeStringToLong(String filesize) {
        if (filesize.contains("MB")) {
            return Long.parseLong(filesize.substring(0, filesize.length()-2)) * 1024*1024;
        } else if (filesize.contains("KB")) {
            return Long.parseLong(filesize.substring(0, filesize.length()-2)) * 1024;
        } else if (filesize.contains("B")) {
            return Long.parseLong(filesize.substring(0, filesize.length()-1));
        }
        return 9999999999L;
    }

}
