package com.boot.learningspirit.controller;


import com.alibaba.excel.EasyExcel;
import com.boot.learningspirit.common.excel.ExcelListener;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.QuestionBank;
import com.boot.learningspirit.service.QuestionBankService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

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


    @PostMapping("upload")
    public Result upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), QuestionBank.class, new ExcelListener(questionBankService)).sheet().doRead();
        return Result.success();
    }
}

