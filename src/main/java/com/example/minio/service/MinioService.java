package com.example.minio.service;

import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  创建接口，并在MiniServiceImpl中实现
 *  这种方式代码逻辑的实现写在MiniServiceImpl中
 */

@Service
public interface MinioService {

    // 创建一个存储桶
    Map<String, Boolean> createBucket(String bucketName);

    // 删除一个存储桶
    Map<String, Boolean> removeBucket(String bucketName);

    // 返回存储桶列表
    Map<String, List<Bucket>> ListBucket();

    // 返回某个储存桶中的所有对象
    Map<String, List<Item>> ListObjects(String bucketName);
}
