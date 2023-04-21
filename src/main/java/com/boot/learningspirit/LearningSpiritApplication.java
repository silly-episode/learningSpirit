package com.boot.learningspirit;

import com.github.jeffreyning.mybatisplus.conf.EnableMPP;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author DYZ89
 */
@SpringBootApplication
@MapperScan("com.boot.learningspirit.dao")
@EnableMPP
@EnableScheduling
public class LearningSpiritApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningSpiritApplication.class, args);
    }

}
