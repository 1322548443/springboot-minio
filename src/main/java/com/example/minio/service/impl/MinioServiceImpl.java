package com.example.minio.service.impl;

import com.example.minio.service.MinioService;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private MinioClient minioClient;

    @Override
    public Map<String, Boolean> createBucket(String bucketName) {
        try {
            if(!minioClient.bucketExists(bucketName)){
                minioClient.makeBucket(bucketName);
                return new HashMap<String, Boolean>(){{
                    put("success", true);
                }};
            }
            else {
                return new HashMap<String, Boolean>(){{
                    put("success", false);
                }};
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Boolean>(){{
            put("failed", false);
        }};
    }

    @Override
    public Map<String, Boolean> removeBucket(String bucketName) {
        try {
            if(minioClient.bucketExists(bucketName)){
                minioClient.removeBucket(bucketName);
                return new HashMap<String, Boolean>(){{
                    put("success", true);
                }};
            }
            else {
                return new HashMap<String, Boolean>(){{
                    put("success", false);
                }};
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new HashMap<String, Boolean>(){{
            put("failed", false);
        }};
    }

    @Override
    public Map<String, List<Bucket>> ListBucket() {
        try {
            return new HashMap<String, List<Bucket>>(){{
                put("success", minioClient.listBuckets());
            }};
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, List<Bucket>>(){{
            put("failed", null);
        }};
    }

    @Override
    public Map<String, List<Item>> ListObjects(String bucketName) {
        try{
            if(minioClient.bucketExists(bucketName)){
                List<Item> listObjects = new ArrayList<>();
                for(Result<Item> myObjects : minioClient.listObjects(bucketName)){
                    listObjects.add(myObjects.get());
                }
                return new HashMap<String, List<Item>>(){{
                    put("success", listObjects);
                }};
            }
            else {
                return new HashMap<String, List<Item>>(){{
                    put("success", null);
                }};
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, List<Item>>(){{
            put("failed", null);
        }};
    }
}
