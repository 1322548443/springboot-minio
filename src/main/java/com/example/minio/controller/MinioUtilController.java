package com.example.minio.controller;

import com.example.minio.service.MinioUtilService;
import com.example.minio.utils.MinioUtils;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/util")
public class MinioUtilController {

    @Autowired
    private MinioUtilService minioUtilService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestParam("bucketName") String buckName ){
        if(StringUtils.isEmpty(buckName)){
            return ResponseEntity.badRequest().body("参数不能为空");
        }

        Map<String, Boolean> map = minioUtilService.createBucket(buckName);
        if(map.containsKey("correct")){
            if(map.get("correct")){
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

        Map<String, Boolean> map = minioUtilService.removeBucket(buckName);
        if(map.containsKey("correct")){
            if(map.get("correct")){
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
        System.out.println("2333");
        Map<String, List<Bucket>> map = minioUtilService.ListBucket();
        if(map.containsKey("correct")){
            return ResponseEntity.ok(map.get("correct"));
        }
        else {
            return ResponseEntity.status(500).body(map.get("wrong"));
        }

    }

    @RequestMapping(value = "/listObjects", method = RequestMethod.GET)
    public ResponseEntity<List<Item>> listObjects(@RequestParam("bucketName") String buckName){
        Map<String, List<Item>> map = minioUtilService.ListObjects(buckName);
        if(map.containsKey("correct")){
            return ResponseEntity.ok(map.get("correct"));
        }
        else {
            return ResponseEntity.status(500).body(map.get("wrong"));
        }
    }
}
