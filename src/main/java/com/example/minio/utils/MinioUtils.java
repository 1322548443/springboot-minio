package com.example.minio.utils;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将minioClient对象注入到该工具类中，使用该工具类中的静态方法操作minio
 * 这样可以直接在service中使用
 */

@Component
public class MinioUtils {

    @Autowired
    private MinioClient minioClient;

    private static MinioUtils minioUtils;

    @PostConstruct
    public void ini() {
        minioUtils = this;
        minioUtils.minioClient = this.minioClient;
    }

    public static Map<String, Boolean> createBucket(String bucketName) {
        try {
            if (!minioUtils.minioClient.bucketExists(bucketName)) {
                minioUtils.minioClient.makeBucket(bucketName);
                return new HashMap<String, Boolean>() {{
                    put("correct", true);
                }};
            } else {
                return new HashMap<String, Boolean>() {{
                    put("correct", false);
                }};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Boolean>() {{
            put("wrong", false);
        }};
    }

    public static Map<String, Boolean> removeBucket(String bucketName) {
        try {
            if(minioUtils.minioClient.bucketExists(bucketName)){
                minioUtils.minioClient.removeBucket(bucketName);
                return new HashMap<String, Boolean>(){{
                    put("correct", true);
                }};
            }
            else {
                return new HashMap<String, Boolean>(){{
                    put("correct", false);
                }};
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new HashMap<String, Boolean>(){{
            put("wrong", false);
        }};
    }

    public static Map<String, List<Bucket>> ListBucket() {
        try {
            return new HashMap<String, List<Bucket>>(){{
                put("correct", minioUtils.minioClient.listBuckets());
            }};
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, List<Bucket>>(){{
            put("wrong", null);
        }};
    }

    public static Map<String, List<Item>> ListObjects(String bucketName) {
        try{
            if(minioUtils.minioClient.bucketExists(bucketName)){
                List<Item> listObjects = new ArrayList<>();
                for(Result<Item> myObjects : minioUtils.minioClient.listObjects(bucketName)){
                    listObjects.add(myObjects.get());
                }
                return new HashMap<String, List<Item>>(){{
                    put("correct", listObjects);
                }};
            }
            else {
                return new HashMap<String, List<Item>>(){{
                    put("correct", null);
                }};
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, List<Item>>(){{
            put("wrong", null);
        }};
    }
}
