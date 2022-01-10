package com.shiyq.imagecloud.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class FileUtil {

    /**
     * 指定路径，文件名（不包含后缀），文件对象，写入新文件；并返回完整文件名
     * @param path 指定路径
     * @param filename 指定文件名
     * @param file 指定文件对象
     * @return 完整文件名
     */
    public static String writeFile(String path, String filename, MultipartFile file) throws IOException {
        // 获取文件后缀名(eg: ".jpg")
        String suffix = Objects.requireNonNull(
                file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        // 完整文件名
        String fullFilename = filename + suffix;
        // 写新文件
        try {
            file.transferTo(new File(path + "/" + fullFilename));
        } catch (IOException e) {
            throw new IOException("写文件【" + path + "/" + fullFilename + "】发生错误！");
        }
        return fullFilename;
    }

    /**
     * 指定路径，文件名（不包含后缀），网络文件地址，写入新文件；并返回完整文件名
     * @param path 指定路径
     * @param filename 指定文件名
     * @param fileUrl 指定网络文件地址
     * @return 完整文件名
     */
    public static String writeWebFile(String path, String filename, String fileUrl) throws IOException {
        // 获取文件后缀名(eg: ".jpg")
        String suffix = FilenameUtils.getName(fileUrl).substring(FilenameUtils.getName(fileUrl).lastIndexOf("."));
        // 完整文件名
        String fullFilename = filename + suffix;
        // 写新文件
        try {
            FileUtils.copyURLToFile(new URL(fileUrl), new File(path + "/" + fullFilename));
        } catch (IOException e) {
            throw new IOException("复制网络文件【"+fileUrl+"】发生错误！");
        }
        return fullFilename;
    }

    /**
     * 创建新路径
     * @return 路径是否已存在
     */
    public static boolean createNewPath(String rootPath, String subPath) throws IOException {
        // 创建原路径（根路径）时不返回true
        File rootPathFile = new File(rootPath);
        if(!rootPathFile.exists()) {
            if (!rootPathFile.mkdirs())
                throw new IOException("创建文件夹【"+rootPathFile+"】发生错误！");
        }
        // 创建子路径时返回true
        File newPathFile = new File(rootPath + subPath);
        if(!newPathFile.exists()) {
            if (!newPathFile.mkdirs())
                throw new IOException("创建文件夹【"+newPathFile+"】发生错误！");
            return true;
        }
        return false;
    }

}
