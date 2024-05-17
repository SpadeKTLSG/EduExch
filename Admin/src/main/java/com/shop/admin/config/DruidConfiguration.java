package com.shop.admin.config;


import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DruidConfiguration {

    //解决druid 日志报错：discard long time none received connection:xxx
    @PostConstruct
    public void setProperties() {
        System.setProperty("druid.mysql.usePingMethod", "false");
    }
}
