package com.shiyq.cloudsystem.handle;

import com.shiyq.cloudsystem.entity.VO.XhrResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(IOException.class)
    public XhrResult IOExceptionHandler(IOException e) {
        System.out.println(e.getMessage());
        // 返回前端IO错误
        return XhrResult.error("An IO exception occurred, please try again later.");
    }

    @ExceptionHandler(Exception.class)
    public XhrResult ExceptionHandler(Exception e) {
        System.out.println(e.getMessage());
        // 返回前端IO错误
        return XhrResult.error("The server is not working properly, please try again later.");
    }

}
