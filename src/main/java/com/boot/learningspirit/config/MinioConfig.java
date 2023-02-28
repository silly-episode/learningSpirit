package com.boot.learningspirit.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/29 7:54
 * @FileName: MinioConfig
 * @Description: minIO配置
 */
@Configuration
public class MinioConfig {
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accesskey}")
    private String accesskey;
    @Value("${minio.secretKey}")
    private String secretKey;


    /**
     * @param :
     * @Return: MinioClient
     * @Author: DengYinzhe
     * @Description: 获取MinioClient
     * @Date: 2023/1/29 13:56
     */
    @Bean
    public MinioClient Client() {
        System.out.println("==========================");
        System.out.println(endpoint);

        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accesskey, secretKey)
                .build();

//        return MinioClient.builder()
//                .endpoint("http://127.0.0.1:9000")
//                .credentials("PJhMHTzyMnN0vjew", "x27mRhN2qgzdIcsYAcvpxzNG1QIRqi4U")
//                .build();
    }
}
