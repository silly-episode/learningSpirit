package com.boot.learningspirit;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class test2 {
    private static final long CHARGING_DURATION = 36000; // 充电时长，单位为毫秒
    private Map<String, Timer> timerMap; // 用于存储用户ID与对应的定时器
    // 可以使用数据库或其他持久化方式替代简单的Map来存储充电任务信息

    public test2() {
        timerMap = new HashMap<>();
    }

    public static void main(String[] args) {
        test2 manager = new test2();
        // 模拟多个用户同时开始充电
        manager.startCharging("user1");
        manager.startCharging("user2");
        manager.startCharging("user3");
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
        timer.schedule(stopChargingTask, CHARGING_DURATION);

        // 将定时器添加到Map中，以便后续管理
        timerMap.put(userId, timer);
    }

    // 停止充电
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
}

