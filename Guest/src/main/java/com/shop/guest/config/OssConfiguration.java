package com.shop.guest.config;


import com.shop.common.properties.AliOssProperties;
import com.shop.common.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于创建AliOssUtil对象配置类
 *
 * @author SK
 * @date 2024/06/08
 */
@Configuration
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean //保证只有一个AliOssUtil对象
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.debug("创建完成阿里云文件上传工具类对象");
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
