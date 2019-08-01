package com.example.minio.controller;

import com.example.minio.service.MinioUtilService;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(value = "minio操作接口")
@RestController
@RequestMapping(value = "/util")
public class MinioUtilController {

    @Autowired
    private MinioUtilService minioUtilService;

    @ApiOperation(value = "创建指定的存储桶", notes = "需要输入合法的存储桶的名称")
    @ApiImplicitParam(name = "bucketName", value = "存储桶的名字", required = true, dataType = "String")
    @ApiResponses({
            @ApiResponse(code = 200, message = "服务器成功处理消息，处理结果显示在返回的body中"),
            @ApiResponse(code = 500, message = "服务器抛出异常")
    })
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

    @ApiOperation(value = "删除指定的存储桶", notes = "删除存储桶却无法删除存储桶中保存的对象")
    @ApiImplicitParam(name = "bucketName", value = "存储桶的名称", required = true, dataType = "String")
    @ApiResponses({
            @ApiResponse(code = 200, message = "服务器成功处理消息，处理结果显示在返回的body中"),
            @ApiResponse(code = 500, message = "服务器抛出异常")
    })
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

    @ApiOperation(value = "列出所有存储桶的名称", notes = "不存在则返回空")
    @ApiResponses({
            @ApiResponse(code = 200, message = "服务器成功处理消息，处理结果显示在返回的body中"),
            @ApiResponse(code = 500, message = "服务器抛出异常")
    })
    @RequestMapping(value = "/listBucket", method = RequestMethod.GET)
    public ResponseEntity<List<Bucket>> listBucket() {
        Map<String, List<Bucket>> map = minioUtilService.ListBucket();
        if(map.containsKey("correct")){
            return ResponseEntity.ok(map.get("correct"));
        }
        else {
            return ResponseEntity.status(500).body(map.get("wrong"));
        }

    }

    @ApiOperation(value = "列出指定存储桶中的所有对象", notes = "存储桶需要存在")
    @ApiImplicitParam(name = "bucketName", value = "存储桶的名称", required = true, dataType = "String")
    @ApiResponses({
            @ApiResponse(code = 200, message = "服务器成功处理消息，处理结果显示在返回的body中"),
            @ApiResponse(code = 500, message = "服务器抛出异常")
    })
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
