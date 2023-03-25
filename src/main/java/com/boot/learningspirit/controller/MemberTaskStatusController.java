package com.boot.learningspirit.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.ClassMember;
import com.boot.learningspirit.entity.MemberTaskStatus;
import com.boot.learningspirit.entity.Task;
import com.boot.learningspirit.service.ClassMemberService;
import com.boot.learningspirit.service.MemberTaskStatusService;
import com.boot.learningspirit.service.TaskService;
import com.boot.learningspirit.service.UserService;
import com.boot.learningspirit.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    JwtUtil jwtUtil;
    @Resource
    private TaskService taskService;
    @Resource
    private UserService userService;
    @Resource
    private ClassMemberService classMemberService;


    @GetMapping("getDetailTask")
    public Result getDetailTask(@RequestParam Long taskId, HttpServletRequest request) {

        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);

//        获取任务的信息
        Task task = taskService.getById(taskId);
        task.setPublisher(userService.getById(openid).getUserName());

//        获取所有的成员的完成情况
        Map<String, Object> map = new HashMap<>(1);
        map.put("task_id", taskId);
        List<MemberTaskStatus> statusList = memberTaskStatusService.listByMap(map);


        List<String> noList = new ArrayList<>(100);
        List<MemberTaskStatus> yesList = new ArrayList<>(100);
        QueryWrapper<ClassMember> queryWrapper = new QueryWrapper<>();
        String type = null;
        for (MemberTaskStatus status : statusList) {
            status.setUserName(userService.getById(status.getOpenId()).getUserName());
            if ("已完成".equals(status.getStatus())) {
                yesList.add(status);
            } else {
                queryWrapper.clear();
                queryWrapper.select("type").eq("open_id", status.getOpenId());
                type = classMemberService.getOne(queryWrapper).getType();
                if ("student".equals(type)) {
                    noList.add(status.getUserName());
                }
            }
        }
        task.setCompletedList(yesList);
        task.setIncompletedList(noList);
        return Result.success(task);
    }

}

