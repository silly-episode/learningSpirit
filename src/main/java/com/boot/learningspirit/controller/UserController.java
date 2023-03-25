package com.boot.learningspirit.controller;


import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.utils.MinIOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;


/**
 * 用户表(User)表控制层
 *
 * @author makejava
 * @since 2023-02-01 11:31:13
 */
@RestController
public class UserController {


    @Resource
    private MinIOUtils minioUtils;

    @GetMapping("/")
    public String test() {
        return "Test";
    }

    @GetMapping("test")
    public Result test(@RequestParam LocalDateTime time) {
        System.out.println(time);
        return Result.success();
    }

    /**
     * @param file:
     * @Return: String
     * @Author: DengYinzhe
     * @Description: TODO
     * @Date: 2023/1/31 8:00
     */
    @PostMapping("upload")
    public Result upload(MultipartFile file) {


        // 判断上传文件是否为空
        if (null == file || 0 == file.getSize()) {
            return Result.error(4010, "文件为空");
        }
        try {
            String bucketName = "spirit";
            // 文件名
            String originalFilename = file.getOriginalFilename();
            // 新的文件名 = 存储桶名称_时间戳.后缀名
            assert originalFilename != null;
            String fileName = bucketName + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
            // 开始上传
            minioUtils.putObject(bucketName, file, fileName);

            return Result.success(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败");

        }
    }


//    @PostMapping
//    public Result login() {
//
//    }


}

