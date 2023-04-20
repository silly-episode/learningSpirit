package com.boot.learningspirit.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.dto.GradeJsonDto;
import com.boot.learningspirit.entity.*;
import com.boot.learningspirit.service.*;
import com.boot.learningspirit.utils.ActionLogUtils;
import com.boot.learningspirit.utils.BeanDtoVoUtils;
import com.boot.learningspirit.utils.JwtUtil;
import com.boot.learningspirit.utils.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
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
    @Resource
    private QuestionBankService questionBankService;
    @Resource
    private MessageService msgService;
    @Resource
    private ActionLogUtils actionLogUtils;

    /**
     * @param taskId:
     * @param request:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 老师详情任务
     * @Date: 2023/3/25 20:13
     */
    @GetMapping("getTeacherDetailTask")
    public Result getTeacherDetailTask(@RequestParam Long taskId, HttpServletRequest request) {

        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);

//        获取任务的信息
        Task task = taskService.getById(taskId);
        task.setPublisher(userService.getById(task.getOpenId()).getUserName());
        if ("exam".equals(task.getType()) && task.getModuleId() != null) {
            QueryWrapper<QuestionBank> questionBankQueryWrapper = new QueryWrapper<>();
            questionBankQueryWrapper
                    .eq("module_id", task.getModuleId());
            List<QuestionBank> questionBanks = questionBankService.list(questionBankQueryWrapper);
            task.setBankName(questionBanks.size() > 0 ? questionBanks.get(1).getModule() : null);
        }
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
                List<ClassMember> classMemberList = classMemberService.list(queryWrapper);
                type = classMemberList.get(0).getType();
                if ("student".equals(type)) {
                    noList.add(status.getUserName());
                }
            }
        }
        task.setCompletedList(yesList);
        task.setIncompletedList(noList);
        return Result.success(task);
    }


    /**
     * @param taskId:
     * @param request:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 学生详情任务
     * @Date: 2023/3/25 20:13
     */
    @GetMapping("getStudentDetailTask")
    public Result getStudentDetailTask(@RequestParam Long taskId, HttpServletRequest request) {
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
//        获取任务的信息
        Task task = taskService.getById(taskId);
        if (task == null) {
            return Result.error(4071, "该任务不存在");
        }
        task.setPublisher(userService.getById(task.getOpenId()).getUserName());
//        获取这个成员的完成情况
        QueryWrapper<MemberTaskStatus> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId).eq("open_id", openid);
        MemberTaskStatus status = memberTaskStatusService.getOne(wrapper);
        if (status != null) {
            status.setUserName(userService.getById(status.getOpenId()).getUserName());
            task.setMemberTaskStatus(status);
            return Result.success(task);
        } else {
            return Result.error(4072, "该任务暂未开放");
        }

    }


    /**
     * @param map:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 录入成绩
     * @Date: 2023/3/26 9:51
     */
    @PostMapping("saveTaskResult")
    public Result saveGrade(@RequestBody Map<String, Object> map,
                            HttpServletRequest request) {
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
        String type = (String) map.get("type");
        UpdateWrapper<MemberTaskStatus> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("open_id", openid)
                .eq("task_id", map.get("taskId"))
                .eq("type", type)
                .set("status", "已完成")
                .set("status_time", LocalDateTime.now());

//        不同的修改
        if ("exam".equals(type)) {
            updateWrapper
                    .set("grade", map.get("grade"))
                    .set("json_str", map.get("jsonStr"));
        } else if ("notice".equals(type)) {
            updateWrapper.set("confirm", true);
        } else if ("jielong".equals(type)) {
            updateWrapper
                    .set("confirm", true)
                    .set("msg", map.get("msg"));
        } else if ("tianbiao".equals(type)) {
            updateWrapper.set("answer_list", map.get("answerList"))
                    .set("file_list", map.get("fileList"));
        } else if ("work".equals(type)) {
            updateWrapper
                    .set("file_list", map.get("fileList"))
                    .set("msg", map.get("msg"));
        }
        System.out.println("===============");
        System.out.println(map.get("msg"));
//    存入数据库
        if (memberTaskStatusService.update(updateWrapper)) {
            return Result.success("录入成功");
        } else {
            return Result.error("录入失败");
        }
    }

    /**
     * @param taskId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 获取某次成绩
     * @Date: 2023/3/28 17:48
     */
    @GetMapping("getGrade")
    public Result getGrade(@RequestParam Long taskId, HttpServletRequest request) {
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
        QueryWrapper<MemberTaskStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("grade,json_str")
                .eq("task_id", taskId)
                .eq("open_id", openid);
        GradeJsonDto gradeJsonDto = BeanDtoVoUtils.convert(memberTaskStatusService.getOne(queryWrapper), GradeJsonDto.class);
        return Result.success(gradeJsonDto);
    }


    /**
     * @param map:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 老师批改作业
     * @Date: 2023/3/26 10:58
     */
    @PostMapping("correctingTask")
    public Result correctingTask(@RequestBody Map<String, Object> map, HttpServletRequest request) {

        if (map.get("statusId") == null) {
            return Result.error("参数异常");
        }
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);

        UpdateWrapper<MemberTaskStatus> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("status_id", map.get("statusId"))
                .set("rate", map.get("rate"))
                .set("remark", map.get("remark"))
                .set("mark_status", true)
                .set("file_list", map.get("fileList"));
        //    存入数据库
        if (memberTaskStatusService.update(updateWrapper)) {

            MemberTaskStatus memberTaskStatus = memberTaskStatusService.getById((Serializable) map.get("statusId"));
            Task userTask = taskService.getById(memberTaskStatus.getTaskId());

            //存入消息
            Long msgId = SnowFlakeUtil.getNextId();
            Message msg = new Message()
                    .setMsgId(msgId)
                    .setMsgContent(userService.getById(openid).getUserName() + "(老师)批改了你的《" + userTask.getTitle() + "》任务，快去看看吧！")
                    .setMsgTitle("任务通知")
                    .setMsgType(4)
                    .setTaskId(userTask.getTaskId())
                    .setTaskType(userTask.getType())
                    .setMessageCreateTime(LocalDateTime.now());
            List<MessageReceive> msgReceiveList = new ArrayList<>(10);
            MessageReceive msgReceive = new MessageReceive()
                    .setMsgId(msgId)
                    .setReceiveOpenId(memberTaskStatus.getOpenId());
            msgReceiveList.add(msgReceive);
            msgService.messageSave(msg, msgReceiveList);


            return Result.success("批改录入成功");
        } else {
            return Result.error("批改录入失败");
        }
    }
}

