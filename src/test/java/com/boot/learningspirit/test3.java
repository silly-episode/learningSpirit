package com.boot.learningspirit;

import com.boot.learningspirit.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/4/21 9:50
 * @FileName: test3
 * @Description:
 */
@SpringBootTest
public class test3 {

    @Resource
    private MessageService messageService;


    @Test

    @Scheduled(cron = "0 0/5 * * * ?")
    public void test() {
        messageService.test();
    }
}
