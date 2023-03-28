package com.boot.learningspirit.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.ApplyClassMember;
import com.boot.learningspirit.entity.BanJi;
import com.boot.learningspirit.entity.ClassMember;
import com.boot.learningspirit.service.ApplyClassMemberService;
import com.boot.learningspirit.service.ClassMemberService;
import com.boot.learningspirit.service.ClassService;
import com.boot.learningspirit.service.MemberTaskStatusService;
import com.boot.learningspirit.utils.JwtUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
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

    /**
     * @param applyClassMember:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 管理员审核是否可以加入该班级
     * @Date: 2023/3/26 15:47
     */
    @PostMapping("judgeClass")
    public Result joinClass(@RequestBody ApplyClassMember applyClassMember) {


        //处理申请则要删除申请表的记录
        QueryWrapper<ApplyClassMember> applyClassMemberQueryWrapper = new QueryWrapper<>();
        applyClassMemberQueryWrapper
                .eq("open_id", applyClassMember.getOpenId())
                .eq("class_id", applyClassMember.getClassId());
        ApplyClassMember applyFromDb = applyClassMemberService.getOne(applyClassMemberQueryWrapper);

//        可以加入班级
        if ("同意加入".equals(applyClassMember.getResult())) {

            //        如果班级已满则不能加入班级
            BanJi banJi = classService.getById(applyClassMember.getClassId());
            if (banJi.getJoined().equals(banJi.getClassNum())) {
                applyFromDb.setResult("班级人数已满");
                applyFromDb.setDeal(true);
                applyClassMemberService.updateById(applyFromDb);
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
                return Result.success("加入班级成功");
            } else {
                applyFromDb.setResult("已处理，但系统错误");
                applyFromDb.setDeal(true);
                applyClassMemberService.updateById(applyFromDb);
                return Result.error("加入班级失败");
            }
        }
//        不能加入班级
        else {

            applyFromDb.setResult("拒绝加入");
            applyFromDb.setDeal(true);
            applyClassMemberService.updateById(applyFromDb);

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
        Boolean flag1 = classMemberService.removeByMap(map);
        Boolean flag2 = memberTaskStatusService.removeByMap(map);
        if (flag1 & flag2) {
            ClassMember classMember = classMemberService.getById(openId);
            BanJi banJi = classService.getById(classId);
            if (!"student".equals(classMember.getType())) {
                banJi.setTeacherCount(banJi.getTeacherCount() - 1);
            }
            banJi.setJoined(banJi.getJoined() - 1);
            classService.updateById(banJi);
            return Result.success("删除该成员成功");
        } else {
            return Result.error("删除失败");
        }

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
//        如果班级已满则不能加入班级
        BanJi banJi = classService.getById(classMember.getClassId());
        if (banJi.getJoined().equals(banJi.getClassNum())) {
            return Result.error(4061, "班级人数已满，无法申请加入该班级");
        }

        classMember.setApplyTime(LocalDateTime.now());
//        申请加入班级
        if (applyClassMemberService.save(classMember)) {
            return Result.success("申请加入班级请求成功");
        } else {
            return Result.error("申请失败");
        }
    }

}

