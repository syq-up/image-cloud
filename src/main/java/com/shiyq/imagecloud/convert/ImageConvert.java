package com.shiyq.imagecloud.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyq.imagecloud.entity.DO.Image;
import com.shiyq.imagecloud.entity.DTO.ImageDTO;
import com.shiyq.imagecloud.entity.DTO.PageDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class ImageConvert {
    public static ImageConvert INSTANCE = Mappers.getMapper(ImageConvert.class);

    // Page对象转化为PageDTO对象
    public abstract PageDTO PageToPageDTO(Page<Image> page);

    // 批量映射 List<Image> --> List<ImageDTO>
    public abstract List<ImageDTO> ImageDOsToDTOs(List<Image> imageList);

    // Image对象转化为ImageDTO对象
    @Mapping(source = "path", target = "url")
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract ImageDTO ImageDOToDTO(Image image);
}
