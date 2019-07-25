package com.example.minio.service;

import com.example.minio.utils.MinioUtils;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MinioUtilService {

    // 创建一个存储桶
    public Map<String, Boolean> createBucket(String bucketName){
        return MinioUtils.createBucket(bucketName);
    }

    // 删除一个存储桶
    public Map<String, Boolean> removeBucket(String bucketName){
        return MinioUtils.removeBucket(bucketName);
    }

    // 返回存储桶列表
    public Map<String, List<Bucket>> ListBucket(){
        return MinioUtils.ListBucket();
    }

    // 返回某个储存桶中的所有对象
    public Map<String, List<Item>> ListObjects(String bucketName){
        return MinioUtils.ListObjects(bucketName);
    }
}
