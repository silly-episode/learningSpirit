package com.boot.learningspirit.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.ClassMember;
import com.boot.learningspirit.entity.Task;
import com.boot.learningspirit.service.ClassMemberService;
import com.boot.learningspirit.service.TaskService;
import com.boot.learningspirit.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


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
    @Autowired
    JwtUtil jwtUtil;
    @Resource
    private ClassMemberService classMemberService;

    /**
     * @param task:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 创建任务
     * @Date: 2023/3/16 10:09
     */
    @PostMapping("create")
    public Result create(
            @RequestBody Task task,
            HttpServletRequest request) {
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
        task.setOpenId(openid);
        taskService.save(task);
        return Result.success();
    }


    @GetMapping("getTaskList")
    public Result getTaskList(@RequestParam String type,
                              HttpServletRequest request) {

        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
        //查询该用户加入的班级id
        QueryWrapper<ClassMember> wrapper = new QueryWrapper<>();
        wrapper.select("class_id").eq("open_id", openid);
        List<ClassMember> list = classMemberService.list(wrapper);


//        查询具体任务列表
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        List<Task> taskList = new ArrayList<>(100);
//        分情况查询
        for (ClassMember member : list) {
            if ("affair".equals(type)) {
                queryWrapper
                        .like("receive_class_list", "%" + member.getClassId() + "%")
                        .and(e -> e.eq("type", "notice")
                                .or().eq("type", "jielong")
                                .or().eq("type", "tianbiao"));
            } else {
                queryWrapper
                        .like("receive_class_list", "%" + member.getClassId() + "%")
                        .and(e -> e.eq("type", "work")
                                .or().eq("type", "exam"));
            }
            taskList.addAll(taskService.list(queryWrapper));
            queryWrapper.clear();
        }
        return Result.success(taskList);
    }

}

