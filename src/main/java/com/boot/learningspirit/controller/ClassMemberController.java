package com.boot.learningspirit.controller;


import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.ClassMember;
import com.boot.learningspirit.service.ClassMemberService;
import com.boot.learningspirit.utils.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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


    @PostMapping("joinClass")
    public Result joinClass(@RequestBody ClassMember classMember, HttpServletRequest request) {
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);
        classMember.setOpenId(openid);
        if (classMemberService.save(classMember)) {
            return Result.success("加入班级成功");
        } else {
            return Result.error("加入班级失败");
        }
    }

}

