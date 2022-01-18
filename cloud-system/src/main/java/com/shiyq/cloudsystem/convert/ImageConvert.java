package com.shiyq.cloudsystem.convert;

import com.shiyq.cloudsystem.entity.DO.Image;
import com.shiyq.cloudsystem.entity.VO.ImageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class ImageConvert {
    public static ImageConvert INSTANCE = Mappers.getMapper(ImageConvert.class);

    // 批量映射 List<Image> --> List<ImageDTO>
    public abstract List<ImageVO> ImageDOs2VOs(List<Image> imageList);

    // Image对象转化为ImageDTO对象
    @Mapping(source = "path", target = "url")
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract ImageVO ImageDO2VO(Image image);
}
