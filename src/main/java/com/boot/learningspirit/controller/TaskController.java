package com.boot.learningspirit.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.*;
import com.boot.learningspirit.service.*;
import com.boot.learningspirit.utils.ActionLogUtils;
import com.boot.learningspirit.utils.JwtUtil;
import com.boot.learningspirit.utils.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 任务表(Task)表控制层
 *
 * @author makejava
 * @since 2023-03-02 14:15:14
 */
@RestController
@RequestMapping("task")
@SuppressWarnings("all")
public class TaskController {
    /**
     * 服务对象
     */
    @Resource
    private ActionLogUtils actionLogUtils;
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
    @Resource
    private MessageService msgService;


    private Map<Long, Timer> timerMap = new HashMap<>();

    //    发布
    public void stopCharging(Long key, Task task, String openid, Map<String, String> map) {

        //        获取班级Id的list
        List<String> banJiList = Arrays.asList(task.getReceiveClassList().split(","));

//        查询班级下的成员id的list
        QueryWrapper<ClassMember> classMemberQueryWrapper = new QueryWrapper<>();
        classMemberQueryWrapper.in("class_id", banJiList);
        List<ClassMember> classMemberList = classMemberService.list(classMemberQueryWrapper);


        //生成消息
        Long msgId = SnowFlakeUtil.getNextId();
        Message msg = new Message()
                .setMsgId(msgId)
                .setMsgContent(userService.getById(openid).getUserName() + "(老师)发布了《" + task.getTitle() + "》任务，快去完成吧！")
                .setMsgTitle("任务通知")
                .setMsgType(3)
                .setTaskId(task.getTaskId())
                .setTaskType(map.get("type"))
                .setMessageCreateTime(LocalDateTime.now());
        List<MessageReceive> msgReceiveList = new ArrayList<>(10);

//        组成最后的要添加的数据（消息和任务完成情况）
        List<MemberTaskStatus> memberTaskStatusList = new ArrayList<>(classMemberList.size());
        for (ClassMember classMember : classMemberList) {
            memberTaskStatusList.add(
                    new MemberTaskStatus(
                            task.getTaskId(), classMember.getOpenId(),
                            LocalDateTime.now(), classMember.getClassId(), task.getType()));

            if ("student".equals(classMember.getType())) {
                MessageReceive msgReceive = new MessageReceive()
                        .setMsgId(msgId)
                        .setReceiveOpenId(classMember.getOpenId());
                msgReceiveList.add(msgReceive);
            }

        }

        memberTaskStatusService.saveBatch(memberTaskStatusList);
        msgService.messageSave(msg, msgReceiveList);


        // 取消定时任务
        Timer timer = timerMap.get(key);
        if (timer != null) {
            timer.cancel();
            timer = null;
            timerMap.remove(key); // 从Map中移除定时器
        }
    }

    // 启动延时任务
    public void startCharging(LocalDateTime fixTime, Task task, String openid, Map<String, String> map) {

        long key = SnowFlakeUtil.getNextId();

        long millisecondsDiff = java.time.Duration.between(LocalDateTime.now(), fixTime).toMillis();

        // 创建定时器
        Timer timer = new Timer();
        // 定义定时任务
        TimerTask stopChargingTask = new TimerTask() {
            @Override
            public void run() {
                stopCharging(key, task, openid, map); // 充电时长到达后停止充电
            }
        };
        // 启动定时任务，设置定时时长
        timer.schedule(stopChargingTask, millisecondsDiff);

        // 将定时器添加到Map中，以便后续管理
        timerMap.put(key, timer);
    }

    //监控截至时间
    public void startDeadLine(LocalDateTime deadLine, Task task, String openid) {

        long key = SnowFlakeUtil.getNextId();

        long millisecondsDiff = java.time.Duration.between(LocalDateTime.now(), deadLine).toMillis();

        // 创建定时器
        Timer timer = new Timer();
        // 定义定时任务
        TimerTask stopChargingTask = new TimerTask() {
            @Override
            public void run() {
                stopDeadLine(key, task, openid);
            }
        };
        // 启动定时任务，设置定时时长
        timer.schedule(stopChargingTask, millisecondsDiff);

        // 将定时器添加到Map中，以便后续管理
        timerMap.put(key, timer);
    }

    public void stopDeadLine(Long key, Task task, String openid) {
        /*发送消息*/
        //生成消息
        Long msgId = SnowFlakeUtil.getNextId();
        Message msg = new Message()
                .setMsgId(msgId)
                .setMsgContent("《" + task.getTitle() + "》任务已截止，快去查看吧！")
                .setMsgTitle("任务通知")
                .setMsgType(10)
                .setTaskId(task.getTaskId())
                .setTaskType(task.getType())
                .setMessageCreateTime(LocalDateTime.now());

        List<MessageReceive> msgReceiveList = new ArrayList<>(10);
        MessageReceive msgReceive = new MessageReceive()
                .setMsgId(msgId)
                .setReceiveOpenId(openid);
        msgReceiveList.add(msgReceive);
        msgService.messageSave(msg, msgReceiveList);

        // 取消定时任务
        Timer timer = timerMap.get(key);
        if (timer != null) {
            timer.cancel();
            timer = null;
            timerMap.remove(key); // 从Map中移除定时器
        }
    }

    /**
     * @param map:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 创建任务
     * @Date: 2023/3/16 10:09
     */
    @PostMapping("create")
    public Result create(
            @RequestBody Map<String, String> map,
            HttpServletRequest request) {

        try {
            Task task = new Task();
            if (map.get("qNumber") != null) {
                task.setQNumber(Integer.valueOf(map.get("qNumber")));
            }
            task.setContent(map.get("content"));
            if (!"".equals(map.get("deadline"))) {
                task.setDeadline(LocalDateTime.parse(map.get("deadline"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if (!"".equals(map.get("fixTime"))) {
                task.setFixTime(LocalDateTime.parse(map.get("fixTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            task.setFileList(map.get("fileList"));
            if ((map.get("isDraft") != null)) {
                task.setIsDraft(Boolean.valueOf(map.get("isDraft")));
            }
            if (map.get("moduleId") != null) {
                task.setModuleId(Long.valueOf(map.get("moduleId")));
            }
            task.setQuestionList(map.get("questionList"));
            if (map.get("random") != null) {
                task.setRandom(Boolean.valueOf(map.get("random")));
            }
            task.setReceiveClassList(map.get("receiveClassList"));
            task.setTitle(map.get("title"));
            task.setType(map.get("type"));
            task.setWay(map.get("way"));
            //获取请求头token
            String token = request.getHeader("Authorization");
            //从token中获取openid
            String openid = jwtUtil.getOpenidFromToken(token);
            task.setOpenId(openid);
            LocalDateTime now = LocalDateTime.now();
//        设置发布时间

            if (task.getFixTime() == null) {
                task.setPublishTime(now);
            } else {
                /*这里为定时发布*/
                task.setPublishTime(task.getFixTime());
            }

//        存入task
            Long taskId = SnowFlakeUtil.getNextId();
            if ("".equals(map.get("taskId"))) {
                task.setTaskId(taskId);
            } else {
                task.setTaskId(Long.valueOf(map.get("taskId")));
            }


            taskService.saveOrUpdate(task);
            if (task.getIsDraft()) {
                return Result.success("保存草稿成功");
            }

            /*下面是不是草稿所做的事情*/
            /*如果是延时发布*/
            if (task.getFixTime() != null) {
                startCharging(task.getFixTime(), task, openid, map);
                return Result.error("发布定时任务成功");
            }

            /*监控DeadLIne*/
            startDeadLine(task.getDeadline(), task, openid);


//        获取班级Id的list
            List<String> banJiList = Arrays.asList(task.getReceiveClassList().split(","));

//        查询班级下的成员id的list
            QueryWrapper<ClassMember> classMemberQueryWrapper = new QueryWrapper<>();
            classMemberQueryWrapper.in("class_id", banJiList);
            List<ClassMember> classMemberList = classMemberService.list(classMemberQueryWrapper);


            //生成消息
            Long msgId = SnowFlakeUtil.getNextId();
            Message msg = new Message()
                    .setMsgId(msgId)
                    .setMsgContent(userService.getById(openid).getUserName() + "(老师)发布了《" + task.getTitle() + "》任务，快去完成吧！")
                    .setMsgTitle("任务通知")
                    .setMsgType(3)
                    .setTaskId(task.getTaskId())
                    .setTaskType(map.get("type"))
                    .setMessageCreateTime(LocalDateTime.now());
            List<MessageReceive> msgReceiveList = new ArrayList<>(10);

//        组成最后的要添加的数据（消息和任务完成情况）
            List<MemberTaskStatus> memberTaskStatusList = new ArrayList<>(classMemberList.size());
            for (ClassMember classMember : classMemberList) {
                memberTaskStatusList.add(
                        new MemberTaskStatus(
                                task.getTaskId(), classMember.getOpenId(),
                                now, classMember.getClassId(), task.getType()));

                if ("student".equals(classMember.getType())) {
                    MessageReceive msgReceive = new MessageReceive()
                            .setMsgId(msgId)
                            .setReceiveOpenId(classMember.getOpenId());
                    msgReceiveList.add(msgReceive);
                }

            }

            memberTaskStatusService.saveBatch(memberTaskStatusList);
            msgService.messageSave(msg, msgReceiveList);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("error");
    }


    /**
     * @param taskId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 删除任务
     * @Date: 2023/3/26 13:10
     */
    @GetMapping("delete")
    @Transactional
    public Result delete(@RequestParam Long taskId, @RequestParam String taskType) {
        Task task = taskService.getById(taskId);
        if ("draft".equals(taskType)) {
            if (!task.getIsDraft()) {
                return Result.error("该任务不是草稿，不能删除");
            } else {
                if (taskService.removeById(taskId)) {
                    return Result.success("删除草稿成功");
                } else {
                    return Result.error("删除草稿失败");
                }
            }
        } else {
            boolean flag1 = taskService.removeById(taskId);

            QueryWrapper<MemberTaskStatus> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("task_id", taskId);

            boolean flag2 = memberTaskStatusService.remove(queryWrapper);

            if (flag1 & flag2) {
                return Result.success("撤销任务成功");
            } else {
                return Result.error("撤销任务失败");
            }
        }
    }

    /**
     * @param type:
     * @param title:
     * @param status:
     * @param isDraft:
     * @param beginDate:
     * @param endDate:
     * @param request:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 任务列表(学生老师草稿)
     * @Date: 2023/3/28 8:53
     */
    @GetMapping("getTaskList")
    public Result getTaskList(@RequestParam String type,
                              @RequestParam String title,
                              @RequestParam String status,
                              @RequestParam Boolean isDraft,
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
            queryWrapper
                    .like(null != title, "title", title)
                    .eq("is_draft", isDraft)
                    .like(!isDraft, "receive_class_list", String.valueOf(member.getClassId()))
                    .eq(isDraft, "open_id", openid)
                    .le(!isDraft && null != endDate, "publish_time", endDate)
                    .ge(!isDraft && null != beginDate, "publish_time", beginDate)
                    .lt(!isDraft, "publish_time", LocalDateTime.now())
                    .orderByDesc("publish_time");
            if ("affair".equals(type)) {
                queryWrapper
                        .and(e -> e.eq("type", "notice")
                                .or().eq("type", "jielong")
                                .or().eq("type", "tianbiao"));
            } else {
                queryWrapper
                        .and(e -> e.eq("type", "work")
                                .or().eq("type", "exam"));
            }
            taskList.addAll(taskService.list(queryWrapper));
            queryWrapper.clear();
        }
        //如果是草稿则根据taskId对taskList进行去重
        if (isDraft) {
            taskList = taskList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                    -> new TreeSet<>(Comparator.comparing(Task::getTaskId))), ArrayList::new));
        }


//        班级信息
        QueryWrapper<BanJi> banJiWrapper = new QueryWrapper<>();
        banJiWrapper.select("class_id,class_name,joined,teacher_count").in("class_id", classIdList);
        List<BanJi> banJiList = null;
        if (classIdList.size() > 0) {
            banJiList = classService.list(banJiWrapper);
        }


        QueryWrapper<MemberTaskStatus> memberTaskStatusQueryWrapper = new QueryWrapper<>();
        //      班级任务完成情况
        memberTaskStatusQueryWrapper
                .select("task_id,class_id,count(status) as count_status")
                .eq("status", "已完成")
                .in("class_id", classIdList)
                .groupBy("class_id", "task_id");
        List<MemberTaskStatus> statusList = null;
        if (classIdList.size() > 0) {
            statusList = memberTaskStatusService.list(memberTaskStatusQueryWrapper);
        }


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

//        删除status为null的任务
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getStatus() == null && !taskList.get(i).getIsDraft()) {
                taskList.remove(i);
                i--;
            }
        }

        return Result.success(taskList);
    }

}

