package com.shiyq.imagecloud.config;

import com.shiyq.imagecloud.interceptor.JWTInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

/**
 * WebMvc配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置上传文件的目录
     */
    @Value("${file.uploadFolder}")
    private String uploadFolder;
    @Value("${file.staticAccessPath}")
    private String staticAccessPath;

    /**
     * 解决跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //设置允许跨域的路径
                .allowedOriginPatterns("*") //设置允许跨域请求的域名
                // 设置允许的方法
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600) //跨域允许时间
                .allowCredentials(true); //是否允许证书 不再默认开启
    }

    /**
     * 配置JWT拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/**");
    }

    /**
     * 配置文件上传路径和请文件大小限制
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(uploadFolder);
        factory.setMaxFileSize(DataSize.parse("5MB"));
        factory.setMaxRequestSize(DataSize.parse("120MB"));
        return factory.createMultipartConfig();
    }

    /**
     * 静态资源对外暴露的访问路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(staticAccessPath).addResourceLocations("file:" + uploadFolder);
    }
}
