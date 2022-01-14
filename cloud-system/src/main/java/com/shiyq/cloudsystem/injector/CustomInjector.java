package com.shiyq.cloudsystem.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        // 官方选装件，逻辑删除时，自动填充其他字段
        methodList.add(new LogicDeleteByIdWithFill());
        return methodList;
    }
}
