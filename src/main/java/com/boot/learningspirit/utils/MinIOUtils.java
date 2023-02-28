package com.boot.learningspirit.utils;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/29 14:07
 * @FileName: MinIOUtils
 * @Description: minIO工具类
 */
@Component
public class MinIOUtils {

    private static final int DEFAULT_EXPIRY_TIME = 7 * 24 * 3600;
    @Autowired
    @Qualifier("Client")
    private MinioClient minioClient;

    /**
     * @param bucketName: 桶名称
     * @Return: boolean
     * @Author: DengYinzhe
     * @Description: 桶是否存在
     * @Date: 2023/1/29 15:04
     */
    public boolean bucketExists(String bucketName) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            BucketExistsArgs args = BucketExistsArgs.builder().bucket(bucketName).build();
            return minioClient.bucketExists(args);
        } catch (MinioException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * @param :
     * @Return: List<String>
     * @Author: DengYinzhe
     * @Description: 列出所有存储桶名称
     * @Date: 2023/1/29 15:58
     */
    @SneakyThrows
    public List<String> listBucketNames() {
        List<Bucket> bucketList = listBuckets();
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * @param :
     * @Return: List<Bucket>
     * @Author: DengYinzhe
     * @Description: 列出所有存储桶
     * @Date: 2023/1/29 15:58
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }


    /**
     * @param bucketName:
     * @Return: List<String>
     * @Author: DengYinzhe
     * @Description: 列出存储桶中的所有对象名称
     * @Date: 2023/1/29 15:58
     */
    @SneakyThrows
    public List<String> listObjectNames(String bucketName) {
        List<String> listObjectNames = new ArrayList<>();
        if (bucketExists(bucketName)) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * @param bucketName:
     * @Return: Iterable<Result < Item>>
     * @Author: DengYinzhe
     * @Description: 列出所有的对象
     * @Date: 2023/1/29 15:57
     */
    @SneakyThrows
    public Iterable<Result<Item>> listObjects(String bucketName) {
        if (bucketExists(bucketName)) {
            ListObjectsArgs args = ListObjectsArgs.builder().bucket(bucketName).build();
            return minioClient.listObjects(args);
        }
        return null;
    }


    /**
     * @param bucketName:
     * @param objectName:
     * @param fileName:
     * @Return: boolean
     * @Author: DengYinzhe
     * @Description: 上传文件但不确认怎么用
     * @Date: 2023/1/29 17:16
     */
    @SneakyThrows
    public boolean putObject(String bucketName, String objectName, String fileName) {
        if (bucketExists(bucketName)) {
            UploadObjectArgs args = UploadObjectArgs.builder().bucket(bucketName).object(objectName).filename(fileName).build();
            minioClient.uploadObject(args);
            StatObjectResponse statObject = statObject(bucketName, objectName);
            return statObject != null && statObject.size() > 0;
        }
        return false;
    }

    /**
     * @param bucketName:
     * @param multipartFile:
     * @param filename:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 接受MultipartFile的文件上传
     * @Date: 2023/1/29 17:14
     */
    @SneakyThrows
    public void putObject(String bucketName, MultipartFile multipartFile, String filename) {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                .contentType(multipartFile.getContentType())
                .build();
        minioClient.putObject(args);
    }


    /**
     * 以流的形式获取一个文件对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        if (bucketExists(bucketName)) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                GetObjectArgs args = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();
                return minioClient.getObject(args);
            }
        }
        return null;
    }

    /**
     * 以流的形式获取一个文件对象（断点下载）
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param offset     起始字节的位置
     * @param size       要读取的长度 (可选，如果无值则代表读到文件结尾)
     * @return
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName, long offset, Long size) {
        if (bucketExists(bucketName)) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                GetObjectArgs args = GetObjectArgs.builder().bucket(bucketName).object(objectName).offset(offset).length(size).build();
                return minioClient.getObject(args);
            }
        }
        return null;
    }

    /**
     * 下载并将文件保存到本地
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param fileName   File name
     * @return
     */
    @SneakyThrows
    public boolean getObject(String bucketName, String objectName, String fileName) {
        if (bucketExists(bucketName)) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                DownloadObjectArgs args = DownloadObjectArgs.builder().bucket(bucketName).object(objectName).filename(fileName).build();
                minioClient.downloadObject(args);
                return true;
            }
        }
        return false;
    }

    /**
     * @param bucketName:
     * @param objectName:
     * @Return: boolean
     * @Author: DengYinzhe
     * @Description: 删除一个对象
     * @Date: 2023/1/29 15:56
     */
    @SneakyThrows
    public boolean removeObject(String bucketName, String objectName) {
        if (bucketExists(bucketName)) {
            RemoveObjectArgs args = RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build();
            minioClient.removeObject(args);
            return true;
        }
        return false;
    }


    /**
     * 生成一个给HTTP GET请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     */
    @SneakyThrows
    public String presignedGetObject(String bucketName, String objectName, Integer expires) {
        String url = "";
        if (bucketExists(bucketName)) {
            if (expires < 1 || expires > DEFAULT_EXPIRY_TIME) {
                return url;
            }
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket("bucketName")
                            .object("objectName")
                            .expiry(expires)
                            .build());
        }
        return url;
    }

    /**
     * 生成一个给HTTP PUT请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行上传，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     */
    @SneakyThrows
    public String presignedPutObject(String bucketName, String objectName, Integer expires) {
        String url = "";
        Map<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("response-content-type", "application/json");
        if (bucketExists(bucketName)) {
            if (expires < 1 || expires > DEFAULT_EXPIRY_TIME) {
                return url;
            }
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expires)
                            .extraQueryParams(reqParams)
                            .build());
        }
        return url;
    }

    /**
     * @param bucketName:
     * @param objectName:
     * @Return: StatObjectResponse
     * @Author: DengYinzhe
     * @Description: 获取对象元数据
     * @Date: 2023/1/29 15:54
     */
    @SneakyThrows
    public StatObjectResponse statObject(String bucketName, String objectName) {
        if (bucketExists(bucketName)) {
            return minioClient.statObject(StatObjectArgs.builder().bucket("bucketName").object("objectName").build());
        }
        return null;
    }


    public boolean downloadFile(String bucketName, String filePath, String originalName, HttpServletResponse response) {
        boolean status = false;
        try {
            InputStream file = getObject(bucketName, filePath);
            if (!StringUtils.isEmpty(originalName)) {
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(originalName, StandardCharsets.UTF_8));
            } else {
                String filename = new String(filePath.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
                response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            }
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = file.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
            file.close();
            servletOutputStream.close();
            status = true;
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return status;
        }
    }
}
