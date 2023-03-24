package com.boot.learningspirit.controller;


import com.boot.learningspirit.service.MemberTaskStatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 个人任务完成情况(MemberTaskStatus)表控制层
 *
 * @author makejava
 * @since 2023-03-24 12:12:11
 */
@RestController
@RequestMapping("memberTaskStatus")
public class MemberTaskStatusController {
    /**
     * 服务对象
     */
    @Resource
    private MemberTaskStatusService memberTaskStatusService;


}

