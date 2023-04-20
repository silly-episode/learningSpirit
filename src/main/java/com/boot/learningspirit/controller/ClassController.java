package com.boot.learningspirit.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.dto.BanJiDto;
import com.boot.learningspirit.dto.ClassPage;
import com.boot.learningspirit.entity.BanJi;
import com.boot.learningspirit.entity.ClassMember;
import com.boot.learningspirit.entity.User;
import com.boot.learningspirit.service.ClassMemberService;
import com.boot.learningspirit.service.ClassService;
import com.boot.learningspirit.service.UserService;
import com.boot.learningspirit.utils.ActionLogUtils;
import com.boot.learningspirit.utils.BeanDtoVoUtils;
import com.boot.learningspirit.utils.JwtUtil;
import com.boot.learningspirit.utils.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班级表(Class)表控制层
 *
 * @author makejava
 * @since 2023-03-01 16:36:03
 */
@RestController
@RequestMapping("class")
public class ClassController {
    @Autowired
    JwtUtil jwtUtil;
    /**
     * 服务对象
     */
    @Resource
    private ClassService classService;

    @Resource
    private UserService userService;

    @Resource
    private ClassMemberService classMemberService;
    @Resource
    private ActionLogUtils actionLogUtils;

    /**
     * @param banJi:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 修改或保存
     * @Date: 2023/4/1 11:19
     */
    @PostMapping("saveOrUpdateClass")
    public Result saveOrUpdateClass(@RequestBody BanJi banJi, HttpServletRequest request) {
        if (banJi.getClassId() == null) {
            Long id = SnowFlakeUtil.getNextId();
            banJi.setClassId(id);
            banJi.setClassCreateTime(LocalDateTime.now());
            banJi.setClassCreator(banJi.getClassAdmin());
            ClassMember classMember = new ClassMember();
            classMember.setClassId(id);
            classMember.setOpenId(banJi.getClassAdmin());
            classMember.setType("teacher");
            classMemberService.save(classMember);
        }
        if (classService.saveOrUpdate(banJi)) {
            if (banJi.getClassId() == null) {
                actionLogUtils.saveActionLog(request, actionLogUtils.INSERT, "新增了" + banJi.getClassName() + "的班级");
            } else {
                actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "修改了" + banJi.getClassName() + "的班级");
            }
            return Result.success();
        } else {
            return Result.error("修改失败");
        }
    }


    /**
     * @param classPage:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 分页查询
     * @Date: 2023/4/1 15:22
     */
    @PostMapping("classPage")
    public Result classPage(@RequestBody ClassPage classPage) {
        Page<BanJi> pageInfo = new Page<>(classPage.getPageNum(), classPage.getPageSize(), 0);
        String queryName = "%" + classPage.getQueryName() + "%";
        System.out.println(queryName);
        List<BanJi> banJiList = classService.classPage(queryName,
                (classPage.getPageNum() - 1) * classPage.getPageSize(),
                classPage.getPageSize());
        if (banJiList.size() > 0) {
            pageInfo.setTotal(banJiList.get(0).getTotalCount());
            pageInfo.setRecords(banJiList);
        }
        return Result.success(pageInfo);
    }


    /*
     * @param classId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 删除班级
     * @Date: 2023/4/1 15:39
     */
    @GetMapping("deleteClass")
    public Result deleteClass(@RequestParam Long classId) {


        return Result.success();
    }


    /**
     * @param banJi:
     * @param request:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 创建班级
     * @Date: 2023/3/1 16:49
     */
    @PostMapping("create")
    public Result createClass(
            @RequestBody BanJi banJi,
            HttpServletRequest request
    ) {

        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
        Long id = SnowFlakeUtil.getNextId();
        banJi.setClassCreator(openid);
        banJi.setClassAdmin(openid);
        banJi.setClassCreateTime(LocalDateTime.now());
        banJi.setClassId(id);
        if (classService.save(banJi)) {
            ClassMember classMember = new ClassMember();
            classMember.setClassId(id);
            classMember.setOpenId(openid);
            classMember.setType("teacher");
            classMemberService.save(classMember);
            return Result.success("创建成功");
        } else {
            return Result.error("创建失败");
        }
    }

    /**
     * @param classId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 根据班级Id获取班级详情
     * @Date: 2023/3/1 20:37
     */
    @GetMapping("getByClassId")
    public Result getClass(@RequestParam Long classId) {
        BanJi banJi = classService.getById(classId);
        if (banJi == null) {
            return Result.error("班级不存在");
        }
        banJi.setClassCreator(userService.getById(banJi.getClassCreator()).getUserName());
        banJi.setAdminName(userService.getById(banJi.getClassAdmin()).getUserName());
        QueryWrapper<ClassMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", banJi.getClassId());
        List<String> studentId = new ArrayList<>();
        List<String> teacherId = new ArrayList<>();
        List<ClassMember> classMembersList = classMemberService.list(queryWrapper);
        for (ClassMember classMember : classMembersList) {
            if ("student".equals(classMember.getType())) {
                studentId.add(classMember.getOpenId());
            } else {
                teacherId.add(classMember.getOpenId());
            }
        }
        List<User> studentName = new ArrayList<>();
        List<User> teacher = new ArrayList<>();
        for (String s : studentId) {
            studentName.add(userService.getById(s));
        }

        for (String s : teacherId) {
            teacher.add(userService.getById(s));
        }

        Map<String, Object> classMsg = new HashMap<>();
        classMsg.put("class", banJi);
        classMsg.put("classMate", studentName);
        classMsg.put("teacherList", teacher);
        Map<String, Object> map = new HashMap<>(100);
        map.put("classInfo", classMsg);
        return Result.success(map);
    }

    /**
     * @param request:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 获取当前用户的班级列表
     * @Date: 2023/3/1 20:39
     */
    @GetMapping("getByUserId")
    public Result getClasses(HttpServletRequest request) {
        //获取请求头token
        String token = request.getHeader("Authorization");
        if (token.isEmpty()) {
            return Result.error("token为空");
        }
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
        try {
            Map map = new HashMap<>();
            List<BanJi> list = classService.getBanJiList(openid);
            List<BanJiDto> list1 = new ArrayList<>();
            for (BanJi banJi : list) {
                BanJiDto banJiDto = BeanDtoVoUtils.convert(banJi, BanJiDto.class);
                banJiDto.setId(banJi.getClassId().toString());
                list1.add(banJiDto);
            }
            map.put("classList", list1);
            return Result.success(map);
        } catch (Exception e) {
            return Result.error("该用户未加入班级");
        }
    }


}

