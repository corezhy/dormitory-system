package com.zhy.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@MapperScan("com.zhy.management.mapper") // 扫描你的 Mapper 包
@ComponentScan(basePackages = {
        "com.zhy.management",  // 当前模块
        "com.zhy.utils"        // 其他模块的包
})
public class ManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class,args);
    }

}
