package com.example.minio.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    // 从配置文件中取出minio相应的配置信息
    @Value("${spring.minio.uri}")
    private String minioUrl;

    @Value("${spring.minio.accessKey}")
    private String access;

    @Value("${spring.minio.secretKey}")
    private String secret;

    // 将连接成功后的对象交给框架管理
    @Bean
    public MinioClient minioClient() {
        try {
            return new MinioClient(minioUrl, access, secret, false);
        } catch (InvalidEndpointException | InvalidPortException e) {
            e.printStackTrace();
        }
        return null;
    }
}
