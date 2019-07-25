package com.example.minio.controller;

import com.example.minio.service.MinioService;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MinioController {

    @Autowired
    public MinioService minioService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestParam("bucketName") String buckName ){
        if(StringUtils.isEmpty(buckName)){
            return ResponseEntity.badRequest().body("参数不能为空");
        }

        Map<String, Boolean> map = minioService.createBucket(buckName);
        if(map.containsKey("success")){
            if(map.get("success")){
                return ResponseEntity.ok().body("创建成功");
            }else {
                return ResponseEntity.ok().body("创建失败");
            }
        }else {
            return ResponseEntity.status(500).body("服务抛出异常");
        }
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ResponseEntity<String> remove(@RequestParam("bucketName") String buckName ){
        if(StringUtils.isEmpty(buckName)){
            return ResponseEntity.badRequest().body("参数不能为空");
        }

        Map<String, Boolean> map = minioService.removeBucket(buckName);
        if(map.containsKey("success")){
            if(map.get("success")){
                return ResponseEntity.ok().body("删除成功");
            }else {
                return ResponseEntity.ok().body("删除失败");
            }
        }else {
            return ResponseEntity.status(500).body("服务抛出异常");
        }
    }

    @RequestMapping(value = "/listBucket", method = RequestMethod.GET)
    public ResponseEntity<List<Bucket>> listBucket() {
        Map<String, List<Bucket>> map = minioService.ListBucket();
        if(map.containsKey("success")){
            return ResponseEntity.ok(map.get("success"));
        }
        else {
            return ResponseEntity.status(500).body(map.get("failed"));
        }

    }

    @RequestMapping(value = "/listObjects", method = RequestMethod.GET)
    public ResponseEntity<List<Item>> listObjects(@RequestParam("bucketName") String buckName){
        Map<String, List<Item>> map = minioService.ListObjects(buckName);
        if(map.containsKey("success")){
            return ResponseEntity.ok(map.get("success"));
        }
        else {
            return ResponseEntity.status(500).body(map.get("failed"));
        }
    }

}
