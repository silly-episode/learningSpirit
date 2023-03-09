package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.TaskDao;
import com.boot.learningspirit.entity.Task;
import com.boot.learningspirit.service.TaskService;
import org.springframework.stereotype.Service;

/**
 * 任务表(Task)表服务实现类
 *
 * @author makejava
 * @since 2023-03-02 14:15:20
 */
@Service("taskService")
public class TaskServiceImpl extends ServiceImpl<TaskDao, Task> implements TaskService {

}

