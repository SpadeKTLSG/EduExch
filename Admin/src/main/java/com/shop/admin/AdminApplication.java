package com.shop.admin;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 管理端启动类
 *
 * @author SK
 * @date 2024/06/05
 */
@Slf4j
@SpringBootApplication
@EnableScheduling //开启定时任务
@ComponentScan("com.shop")
@EnableAspectJAutoProxy()
@MapperScan(basePackages = {"com.shop.serve.mapper"})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
