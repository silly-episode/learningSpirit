package com.boot.learningspirit.controller;

import com.boot.learningspirit.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/4/21 10:58
 * @FileName: TestController
 * @Description:
 */
@RestController
@RequestMapping("test")
@SuppressWarnings("all")
public class TestController {

    private Map<String, Timer> timerMap = new HashMap<>();

    public void stopCharging(String userId) {
        System.out.println("用户 " + userId + " 停止充电");

        // 取消定时任务
        Timer timer = timerMap.get(userId);
        if (timer != null) {
            timer.cancel();
            timer = null;
            timerMap.remove(userId); // 从Map中移除定时器
        }
    }


    // 开始充电
    public void startCharging(String userId) {
        System.out.println("用户 " + userId + " 开始充电");

        // 判断用户是否已经在充电中
        if (timerMap.containsKey(userId)) {
            System.out.println("用户 " + userId + " 已经在充电中");
            return;
        }

        // 创建定时器
        Timer timer = new Timer();
        // 定义定时任务
        TimerTask stopChargingTask = new TimerTask() {
            @Override
            public void run() {
                stopCharging(userId); // 充电时长到达后停止充电
            }
        };
        // 启动定时任务，设置定时时长
        timer.schedule(stopChargingTask, 10000);

        // 将定时器添加到Map中，以便后续管理
        timerMap.put(userId, timer);
    }

    @GetMapping("ttt")
    public Result test() {

        startCharging("1");
        System.out.println("+++++");
        return Result.success();
    }


}
