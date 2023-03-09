package com.boot.learningspirit.controller;


import com.boot.learningspirit.service.ClassMemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

}

