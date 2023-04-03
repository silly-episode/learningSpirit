package com.boot.learningspirit.config;

import com.boot.learningspirit.common.exception.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/2/20 10:07
 * @FileName: InterceptorConfig
 * @Description:
 */
@Configuration
@SuppressWarnings("all")
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public JwtInterceptor getJwtInterceptor() {
        return new JwtInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getJwtInterceptor())
                .addPathPatterns("/a/b") //拦截用户接口
                .excludePathPatterns("/**");//登录接口不拦截
    }

}
