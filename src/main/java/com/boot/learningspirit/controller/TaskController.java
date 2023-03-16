package com.boot.learningspirit.controller;


import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.Task;
import com.boot.learningspirit.service.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务表(Task)表控制层
 *
 * @author makejava
 * @since 2023-03-02 14:15:14
 */
@RestController
@RequestMapping("task")
public class TaskController {
    /**
     * 服务对象
     */
    @Resource
    private TaskService taskService;

    /**
     * @param task:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 创建任务
     * @Date: 2023/3/16 10:09
     */
    @PostMapping("create")
    public Result create(@RequestBody Task task) throws Exception {

        taskService.save(task);
        return Result.success();

    }
}

