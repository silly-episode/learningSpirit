package com.boot.learningspirit.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.dto.TeacherDto;
import com.boot.learningspirit.entity.*;
import com.boot.learningspirit.service.*;
import com.boot.learningspirit.utils.BeanDtoVoUtils;
import com.boot.learningspirit.utils.JwtUtil;
import com.boot.learningspirit.utils.SnowFlakeUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班级成员表(ClassMember)表控制层
 *
 * @author makejava
 * @since 2023-03-01 20:46:51
 */
@RestController
@RequestMapping("classMember")
public class ClassMemberController {
    /**
     * 服务对象
     */
    @Resource
    private ClassMemberService classMemberService;
    @Resource
    JwtUtil jwtUtil;
    @Resource
    private ClassService classService;
    @Resource
    private ApplyClassMemberService applyClassMemberService;
    @Resource
    private MemberTaskStatusService memberTaskStatusService;
    @Resource
    private MessageService msgService;
    @Resource
    private UserService userService;
    @Resource
    private MessageReceiveService msgReceiveService;


    /**
     * @param classId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 获取班级人员列表
     * @Date: 2023/4/1 11:06
     */
    @PostMapping("getClassMemberList")
    public Result getClassMemberList(@RequestBody Map<String, Long> map) {
        Long classId = map.get("classId");
        if (classId == null) {
            return Result.error("参数错误");
        }
//        初始化
        List<User> studentUserList = new ArrayList<>();
        List<User> teacherUserList = new ArrayList<>();
        Map<String, List<User>> data = new HashMap<>();
//        查询班级所有成员
        QueryWrapper<ClassMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", classId);
        List<ClassMember> classMemberList = classMemberService.list(queryWrapper);

//        查询用户详细和分组数据
        for (ClassMember member : classMemberList) {
            User user = userService.getById(member.getOpenId());
            if ("student".equals(member.getType())) {
                studentUserList.add(user);
            } else {
                teacherUserList.add(user);
            }
        }
//        拼装数据
        data.put("studentUserList", studentUserList);
        data.put("teacherUserList", teacherUserList);
        return Result.success(data);
    }

    /**
     * @param classId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 获取班级老师
     * @Date: 2023/4/1 11:28
     */
    @GetMapping("getClassTeacher")
    public Result getClassTeacher(@RequestParam Long classId) {
        QueryWrapper<ClassMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", classId).eq("type", "teacher");
        List<ClassMember> classMemberList = classMemberService.list(queryWrapper);
        List<TeacherDto> teacherList = new ArrayList<>(100);
        for (ClassMember member : classMemberList) {
            User user = userService.getById(member);
            if (user != null) {
                teacherList.add(BeanDtoVoUtils.convert(user, TeacherDto.class));
            }
        }
        return Result.success(teacherList);
    }

    /**
     * @param applyClassMember:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 管理员审核是否可以加入该班级
     * @Date: 2023/3/26 15:47
     */
    @PostMapping("judgeClass")
    public Result joinClass(@RequestBody ApplyClassMember applyClassMember) {

        //更改消息中的是否处理字段
        UpdateWrapper<MessageReceive> messageReceiveUpdateWrapper = new UpdateWrapper<>();
        messageReceiveUpdateWrapper
                .eq("msg_receive_id", applyClassMember.getMsgReceiveId())
                .set("deal", true);
        msgReceiveService.update(messageReceiveUpdateWrapper);

        //处理申请不要删除申请表的记录
        QueryWrapper<ApplyClassMember> applyClassMemberQueryWrapper = new QueryWrapper<>();
        applyClassMemberQueryWrapper
                .eq("open_id", applyClassMember.getOpenId())
                .eq("class_id", applyClassMember.getClassId())
                .eq("deal", false);
        ApplyClassMember applyFromDb = applyClassMemberService.getOne(applyClassMemberQueryWrapper);

        //将结果发送消息到申请人
        Long msgId = SnowFlakeUtil.getNextId();
        Message msg = new Message()
                .setMsgId(msgId)
                .setMsgTitle("审核通知")
                .setMsgType(2)
                .setMessageCreateTime(LocalDateTime.now());
        List<MessageReceive> msgReceiveList = new ArrayList<>(10);
        MessageReceive msgReceive = new MessageReceive()
                .setMsgId(msgId)
                .setReceiveOpenId(applyClassMember.getOpenId());
        msgReceiveList.add(msgReceive);

//        班级基础信息
        BanJi banJi = classService.getById(applyClassMember.getClassId());

//        可以加入班级
        if ("同意加入".equals(applyClassMember.getResult())) {

            //        如果班级已满则不能加入班级

            if (banJi.getJoined().equals(banJi.getClassNum())) {
                applyFromDb.setResult("班级人数已满");
                applyFromDb.setDeal(true);
                applyClassMemberService.updateById(applyFromDb);
                //存消息
                msg.setMsgContent("班级人数已满,无法加入 " + banJi.getClassName());
                msgService.messageSave(msg, msgReceiveList);

                return Result.error(4061, "班级人数已满,无法同意加入该班级");
            }
            ClassMember classMember = new ClassMember();
            classMember.setClassId(applyClassMember.getClassId());
            classMember.setType(applyClassMember.getType());
            classMember.setOpenId(applyClassMember.getOpenId());
            //  加入班级
            if (classMemberService.save(classMember)) {
                applyFromDb.setResult("成功加入");
                applyFromDb.setDeal(true);
                applyClassMemberService.updateById(applyFromDb);
                if (!"student".equals(classMember.getType())) {
                    banJi.setTeacherCount(banJi.getTeacherCount() + 1);
                }
                banJi.setJoined(banJi.getJoined() + 1);
                classService.updateById(banJi);
                //存消息
                msg.setMsgContent("加入 " + banJi.getClassName() + " 审核已通过");
                msgService.messageSave(msg, msgReceiveList);

                return Result.success("加入班级成功");
            } else {
                applyFromDb.setResult("已处理，但系统错误");
                applyFromDb.setDeal(true);
                applyClassMemberService.updateById(applyFromDb);

                //存消息
                msg.setMsgContent("系统错误，加入 " + banJi.getClassName() + " 失败");
                msgService.messageSave(msg, msgReceiveList);

                return Result.error("加入班级失败");
            }
        }
//        不能加入班级
        else {

            applyFromDb.setResult("拒绝加入");
            applyFromDb.setDeal(true);
            applyClassMemberService.updateById(applyFromDb);


            //存消息
            msg.setMsgContent("加入 " + banJi.getClassName() + " 审核未通过");
            msgService.messageSave(msg, msgReceiveList);
            return Result.success("拒绝加入");
        }
    }

    /**
     * @param openId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 删除成员
     * @Date: 2023/3/26 15:53
     */
    @GetMapping("deleteMember")
    @Transactional
    public Result deleteMember(@RequestParam String openId, @RequestParam Long classId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("open_id", openId);
        map.put("class_id", classId);

        QueryWrapper<ClassMember> c = new QueryWrapper<>();
        c.eq("open_id", openId).eq("class_id", classId);
        ClassMember classMember = classMemberService.getOne(c);
        BanJi banJi = classService.getById(classId);
        if (!"student".equals(classMember.getType())) {
            banJi.setTeacherCount(banJi.getTeacherCount() - 1);
        }
        banJi.setJoined(banJi.getJoined() - 1);
        classService.updateById(banJi);


        //发送删除的消息
        Long msgId = SnowFlakeUtil.getNextId();
        Message msg = new Message()
                .setMsgId(msgId)
                .setMsgContent("已被管理员移出" + banJi.getClassName())
                .setMsgTitle("退出班级通知")
                .setMsgType(5)
                .setMessageCreateTime(LocalDateTime.now());
        List<MessageReceive> msgReceiveList = new ArrayList<>(10);
        MessageReceive msgReceive = new MessageReceive()
                .setMsgId(msgId)
                .setReceiveOpenId(openId);
        msgReceiveList.add(msgReceive);
        msgService.messageSave(msg, msgReceiveList);
        Boolean flag1 = classMemberService.removeByMap(map);
        Boolean flag2 = memberTaskStatusService.removeByMap(map);
        return Result.success("删除该成员成功");
    }

    /**
     * @param applyId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 删除加入班级请求
     * @Date: 2023/3/26 16:47
     */
    @GetMapping("deleteApply")
    public Result deleteApply(@RequestParam Long applyId) {

        // todo 删除正在审核的申请加入班级的消息
        ApplyClassMember apply = applyClassMemberService.getById(applyId);
        if ("正在审核".equals(apply.getResult())) {
            QueryWrapper<Message> wrapper = new QueryWrapper<>();
            QueryWrapper<MessageReceive> queryWrapper = new QueryWrapper<>();
            wrapper
                    .eq("open_id", apply.getOpenId())
                    .eq("class_id", apply.getClassId());
            List<Message> msgList = msgService.list(wrapper);
            for (Message msg : msgList) {
                queryWrapper.clear();
                queryWrapper
                        .eq("msg_id", msg.getMsgId())
                        .and(e -> e.isNull("deal").or().eq("deal", false));
                MessageReceive msgReceive = msgReceiveService.getOne(queryWrapper);
                if (msgReceive != null && (msgReceive.getDeal() == null || !msgReceive.getDeal())) {
                    msgReceiveService.removeById(msgReceive.getMsgReceiveId());
                    msgService.removeById(msgReceive.getMsgId());
                }
            }
        }

        if (applyClassMemberService.removeById(applyId)) {
            return Result.success("删除加入请求班级成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * @param pageSize:
     * @param pageNum:
     * @param request:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 获取申请列表
     * @Date: 2023/3/28 8:45
     */
    @GetMapping("getApplyList")
    public Result getApplyList(
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNum,
            HttpServletRequest request
    ) {
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
        Page<ApplyClassMember> pageInfo = new Page<>(pageNum, pageSize);
        QueryWrapper<ApplyClassMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("open_id", openid);
        applyClassMemberService.page(pageInfo, queryWrapper);
        for (ApplyClassMember record : pageInfo.getRecords()) {
            record.setClassName(classService.getById(record.getClassId()).getClassName());
        }
        return Result.success(pageInfo);
    }


    /**
     * @param classMember:
     * @param request:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 申请加入班级
     * @Date: 2023/3/26 15:47
     */
    @PostMapping("joinClass")
    public Result applyJoinClass(@RequestBody ApplyClassMember classMember, HttpServletRequest request) {
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
        classMember.setOpenId(openid);

//        判断其是否已经加入过该班级
        QueryWrapper<ClassMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("open_id", openid);
        ClassMember classMemberFromDb = classMemberService.getOne(queryWrapper);
        if (classMemberFromDb != null && classMemberFromDb.getClassId().equals(classMember.getClassId())) {
            return Result.error(4060, "你已经加入了该班级");
        }
        QueryWrapper<ApplyClassMember> applyClassMemberQueryWrapper = new QueryWrapper<>();
        applyClassMemberQueryWrapper
                .eq("open_id", classMember.getOpenId())
                .eq("class_id", classMember.getClassId())
                .eq("result", "正在审核");

        if (applyClassMemberService.getOne(applyClassMemberQueryWrapper) != null) {
            return Result.error(4059, "你的申请在审核中");
        }


//        如果班级已满则不能加入班级
        BanJi banJi = classService.getById(classMember.getClassId());
        if (banJi != null && banJi.getJoined().equals(banJi.getClassNum())) {
            return Result.error(4061, "班级人数已满，无法申请加入该班级");
        }

        classMember.setApplyTime(LocalDateTime.now());
//        申请加入班级
        if (applyClassMemberService.save(classMember)) {

//发送申请消息给班级管理员
            Long msgId = SnowFlakeUtil.getNextId();
            Message msg = new Message()
                    .setMsgId(msgId)
                    .setMsgContent(userService.getById(openid).getUserName() +
                            "申请加入" + banJi.getClassName())
                    .setMsgTitle("审核通知")
                    .setMsgType(1)
                    .setMessageCreateTime(LocalDateTime.now())
                    .setOpenId(classMember.getOpenId())
                    .setType(classMember.getType())
                    .setClassId(classMember.getClassId());
            List<MessageReceive> msgReceiveList = new ArrayList<>(10);
            MessageReceive msgReceive = new MessageReceive()
                    .setMsgId(msgId)
                    .setReceiveOpenId(banJi.getClassAdmin());
            msgReceiveList.add(msgReceive);
            msgService.messageSave(msg, msgReceiveList);


            return Result.success("申请加入班级请求成功");
        } else {
            return Result.error("申请失败");
        }
    }

}

