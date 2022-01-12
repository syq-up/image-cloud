package com.shiyq.cloudsystem;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

/**
 * @author shiyq
 * @create 2021-05-03 22:40
 */
public class CodeGenerator {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("shiyq");          // 作者
        gc.setOpen(false);              // 生成后是否打开资源管理器
        gc.setFileOverride(false);      // 重新生成时，是否覆盖原文件
        gc.setServiceName("%sService");     // 去掉Service接口的首字母I
        gc.setIdType(IdType.INPUT);          // 主键生成策略
        gc.setDateType(DateType.ONLY_DATE); // 定义生成的实体类中日期类型。ONLY_DATE：java.util.date
//        gc.setSwagger2(true);           // 开启 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/image-cloud?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("");           // 模块名
        pc.setParent("com.shiyq.imagecloud");      // 包的根路径
        pc.setEntity("entity.DO");         // 实体类根目录
        pc.setMapper("mapper");         // mapper层根目录
        pc.setController("controller"); // 控制器根目录
        pc.setService("service");       // 服务层根目录
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("qqq_qq");
        strategy.setNaming(NamingStrategy.underline_to_camel);      // 数据库表映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);// 数据库表字段映射到实体属性的命名策略
        strategy.setEntityLombokModel(true);    // lombok
        strategy.setEntityTableFieldAnnotationEnable(true); // 开启生成实体时生成字段注解
        strategy.setRestControllerStyle(true);  // 增加注解 @RestController
        strategy.setControllerMappingHyphenStyle(true); // url中驼峰转连字符
        strategy.setLogicDeleteFieldName("deleted");   // 说明逻辑删除字段名
        // 自动填充
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(new TableFill("create_time", FieldFill.INSERT));
        tableFills.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));
        strategy.setTableFillList(tableFills);
        // 配置实体类的继承
        //strategy.setSuperEntityClass("com.shiyq.entity.BaseEntity");            // 设置BaseEntity的位置
        //strategy.setSuperEntityColumns("create_time", "update_time", "remark"); // 填写BaseEntity中的公共字段

        mpg.setStrategy(strategy);

        mpg.execute();
    }

}
