package com.boot.learningspirit.controller;

import com.boot.learningspirit.service.ApplyClassMemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/3/26 15:26
 * @FileName: ApplyClassMemberController
 * @Description:
 */
@RestController
@RequestMapping("applyClass")
public class ApplyClassMemberController {
    @Resource
    private ApplyClassMemberService applyClassMemberService;


}
