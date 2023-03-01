package com.boot.learningspirit.controller;


import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.BanJi;
import com.boot.learningspirit.service.ClassService;
import com.boot.learningspirit.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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

    /**
     * @param banJi:
     * @param request:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 创建班级
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
        banJi.setClassCreator(Long.valueOf(openid));
        banJi.setClassAdmin(Long.valueOf(openid));
        banJi.setClassCreateTime(LocalDateTime.now());

        if (classService.save(banJi)) {
            return Result.success("创建成功");
        } else {
            return Result.error("创建失败");
        }
    }


}

