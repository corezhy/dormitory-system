package com.zhy.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@MapperScan("com.zhy.management.mapper") // 扫描你的 Mapper 包
@ComponentScan(basePackages = {
        "com.zhy.management",  // 当前模块
        "com.zhy.utils"        // 其他模块的包
})
@EnableScheduling //启用 Spring Task 定时任务
public class ManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class,args);
    }

}
