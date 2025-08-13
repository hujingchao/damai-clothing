package com.fenglei.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 启动类
 * @Author zhouyiqiu
 * @Date 2021/12/2 12:52
 * @Version 1.0
 * @Description
 */
@SpringBootApplication
@MapperScan({"com.fenglei.mapper","com.fenglei.security.mapper"})
@ComponentScan(value = "com.fenglei")
@EnableSwagger2
@EnableScheduling
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class);
    }
}

