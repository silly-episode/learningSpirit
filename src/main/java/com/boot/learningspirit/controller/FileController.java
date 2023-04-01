package com.boot.learningspirit.controller;


import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.Task;
import com.boot.learningspirit.utils.MinIOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


/**
 * 用户表(User)表控制层
 *
 * @author makejava
 * @since 2023-02-01 11:31:13
 */
@RestController
public class FileController {


    @Resource
    private MinIOUtils minioUtils;

    @GetMapping("a")
    public String test() {
        return "Test";
    }

    @PostMapping("test")
    public Result AAA(@RequestBody Task a) {
        System.out.println(a.getQNumber());
        return Result.success(a.getQNumber());
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


    @GetMapping("download")
    public void userImage(@RequestParam String filePath, HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            //输出流，通过输出流将文件写回浏览器
            outputStream = response.getOutputStream();
//            response.setContentType("image/jpeg");
//            if ("jpg".equals(filePath.substring(filePath.lastIndexOf(".")))) {
//                response.setContentType("image/jpeg");
//            } else if ("vedio".equals("")) {
//
//            }

            //从MinIo中获取用户头像
            String bucketName = "spirit";
            inputStream = minioUtils.getObject(bucketName, filePath);

            if (inputStream != null) {
                int len;
                byte[] bytes = new byte[1024 * 4];
                while ((len = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                    outputStream.flush();
                }
            }
        } catch (IOException ignored) {
        }
    }


}

