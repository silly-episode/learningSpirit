package com.boot.learningspirit.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.BanJi;
import com.boot.learningspirit.entity.ClassMember;
import com.boot.learningspirit.entity.MemberTaskStatus;
import com.boot.learningspirit.entity.Task;
import com.boot.learningspirit.service.*;
import com.boot.learningspirit.utils.JwtUtil;
import com.boot.learningspirit.utils.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Resource
    private ClassService classService;
    @Resource
    private UserService userService;
    @Resource
    private MemberTaskStatusService memberTaskStatusService;

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
        LocalDateTime now = LocalDateTime.now();
//        设置发布时间
        System.out.println(task.getFixTime());
        System.out.println(task.getFixTime() == null);
        if (task.getFixTime() == null) {
            task.setPublishTime(now);
        } else {
            task.setPublishTime(task.getFixTime());
        }

//        存入task
        Long taskId = SnowFlakeUtil.getNextId();
        task.setTaskId(taskId);
        taskService.save(task);

//        获取班级Id的list
        List<String> banJiList = Arrays.asList(task.getReceiveClassList().split(","));

//        查询班级下的成员id的list
        QueryWrapper<ClassMember> classMemberQueryWrapper = new QueryWrapper<>();
        classMemberQueryWrapper.in("class_id", banJiList);
        List<ClassMember> classMemberList = classMemberService.list(classMemberQueryWrapper);

        System.out.println(classMemberList.toString());

//        组成最后的要添加的数据
        List<MemberTaskStatus> memberTaskStatusList = new ArrayList<>(classMemberList.size());
        for (ClassMember classMember : classMemberList) {
            memberTaskStatusList.add(
                    new MemberTaskStatus(
                            taskId, classMember.getOpenId(),
                            now, classMember.getClassId()));
        }

        memberTaskStatusService.saveBatch(memberTaskStatusList);
        return Result.success();
    }


    @GetMapping("delete")
    public Result delete(@RequestParam Long taskId) {

        return Result.success();
    }


    @GetMapping("getTaskList")
    public Result getTaskList(@RequestParam String type,
                              @RequestParam String title,
                              @RequestParam String status,
                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginDate,
                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                              HttpServletRequest request) {
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);

        //对参数进行处理
        if ("全部".equals(status)) {
            status = null;
        }
        System.out.println("============");
        System.out.println(status);

        //查询该用户加入的班级id
        QueryWrapper<ClassMember> wrapper = new QueryWrapper<>();
        wrapper.select("class_id").eq("open_id", openid);
        List<ClassMember> list = classMemberService.list(wrapper);
//        得到classId的数据集合
        List<Long> classIdList = new ArrayList<>();
        for (ClassMember member : list) {
            classIdList.add(member.getClassId());
        }




//        查询具体任务列表
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        List<Task> taskList = new ArrayList<>(100);
//        分情况查询
        for (ClassMember member : list) {
            if ("affair".equals(type)) {
                queryWrapper
                        .like(null != title, "title", title)
                        .eq("is_draft", false)
                        .like("receive_class_list", String.valueOf(member.getClassId()))
                        .le(null != endDate, "publish_time", endDate)
                        .ge(null != beginDate, "publish_time", beginDate)
                        .and(e -> e.eq("type", "notice")
                                .or().eq("type", "jielong")
                                .or().eq("type", "tianbiao"));
            } else {
                queryWrapper
                        .like(null != title, "title", title)
                        .eq("is_draft", false)
                        .like("receive_class_list", String.valueOf(member.getClassId()))
                        .le(null != endDate, "publish_time", endDate)
                        .ge(null != beginDate, "publish_time", beginDate)
                        .and(e -> e.eq("type", "work")
                                .or().eq("type", "exam"));
            }
            taskList.addAll(taskService.list(queryWrapper));
            queryWrapper.clear();
        }


//        班级信息
        QueryWrapper<BanJi> banJiWrapper = new QueryWrapper<>();
        banJiWrapper.select("class_id,class_name,joined,teacher_count").in("class_id", classIdList);
        List<BanJi> banJiList = classService.list(banJiWrapper);


        QueryWrapper<MemberTaskStatus> memberTaskStatusQueryWrapper = new QueryWrapper<>();
        //      班级任务完成情况
        memberTaskStatusQueryWrapper
                .select("task_id,class_id,count(status) as count_status")
                .eq("status", "已完成")
                .in("class_id", classIdList)
                .groupBy("class_id", "task_id");
        List<MemberTaskStatus> statusList = memberTaskStatusService.list(memberTaskStatusQueryWrapper);


        //        个人完成情况
        memberTaskStatusQueryWrapper.clear();
        memberTaskStatusQueryWrapper
                .eq("open_id", openid)
                .eq(status != null, "status", status);
        List<MemberTaskStatus> personStatusList = memberTaskStatusService.list(memberTaskStatusQueryWrapper);


//      处理数据
        DecimalFormat df = new DecimalFormat("#.00");
        for (Task task : taskList) {
//            添加班级相关信息
            for (BanJi banJi : banJiList) {
                if (task.getReceiveClassList().contains(String.valueOf(banJi.getClassId()))) {
                    task.setClassId(banJi.getClassId());
                    task.setClassName(banJi.getClassName());
                    task.setJoined(banJi.getJoined() - banJi.getTeacherCount());
                }
            }
//            添加发布人相关信息
            task.setPublisher(userService.getById(task.getOpenId()).getUserName());
//            添加班级完成情况相关信息
            for (MemberTaskStatus memberTaskStatus : statusList) {
                if (task.getTaskId().equals(memberTaskStatus.getTaskId()) && task.getClassId().equals(memberTaskStatus.getClassId())) {
                    task.setCompleteNum(memberTaskStatus.getCountStatus());
                    System.out.println(task.getJoined());
                    System.out.println(memberTaskStatus.getCountStatus());
                    System.out.println(df.format(memberTaskStatus.getCountStatus() * 1.0 / task.getJoined()));
                    task.setIncompleteNum(task.getJoined() - memberTaskStatus.getCountStatus());
                    task.setCompletionRate("0" +
                            df.format(memberTaskStatus.getCountStatus() * 1.0 / task.getJoined()));
                }
            }
//            如果一个人都没完成则特殊处理
            if (task.getCompleteNum() == 0 && task.getIncompleteNum() == 0) {
                task.setCompleteNum(0);
                task.setIncompleteNum(task.getJoined());
                task.setCompletionRate("0.00");
            }
//            添加个人完成情况信息
            for (MemberTaskStatus memberTaskStatus : personStatusList) {
                if (memberTaskStatus.getTaskId().equals(task.getTaskId())) {
                    task.setStatus(memberTaskStatus.getStatus());
                }
            }
        }
        return Result.success(taskList);
    }

}

