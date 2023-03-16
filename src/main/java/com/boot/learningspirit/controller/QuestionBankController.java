package com.boot.learningspirit.controller;


import com.boot.learningspirit.service.QuestionBankService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 题库(QuestionBank)表控制层
 *
 * @author makejava
 * @since 2023-03-16 10:37:37
 */
@RestController
@RequestMapping("questionBank")
public class QuestionBankController {
    /**
     * 服务对象
     */
    @Resource
    private QuestionBankService questionBankService;


}

