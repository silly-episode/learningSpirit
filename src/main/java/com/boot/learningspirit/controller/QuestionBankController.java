package com.boot.learningspirit.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.learningspirit.common.excel.ExcelListener;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.entity.QuestionBank;
import com.boot.learningspirit.service.QuestionBankService;
import com.boot.learningspirit.utils.SnowFlakeUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    public Result upload(MultipartFile file, @RequestParam String name) throws IOException {
        String module = name.substring(0, name.lastIndexOf("."));
        Long moduleId = SnowFlakeUtil.getNextId();
        EasyExcel.read(
                        file.getInputStream(),
                        QuestionBank.class,
                        new ExcelListener(questionBankService, moduleId, module))
                .sheet().doRead();
        return Result.success();
    }

    /**
     * @param :
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 获取题库列表
     * @Date: 2023/3/21 11:30
     */
    @GetMapping("getBankList")
    public Result getBankList() {
        QueryWrapper<QuestionBank> wrapper = new QueryWrapper<>();
        wrapper
                .select("module_id , module ,question_create_time ,count(1) as bank_count")
                .groupBy("module_id", "module", "question_create_time")
                .orderByDesc("question_create_time");
        List<QuestionBank> list = questionBankService.list(wrapper);
//        根据QuestionBank中的moduleId对list去重
        list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                -> new TreeSet<>(Comparator.comparing(QuestionBank::getModuleId))), ArrayList::new));
        return Result.success(list);
    }

    /**
     * @param pageNum:
     * @param pageSize:
     * @param moduleId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 获取某个题库的题目
     * @Date: 2023/3/21 11:46
     */
    @GetMapping("getQuestionList")
    public Result getQuestionList(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam Long moduleId) {
        System.out.println(moduleId);
        Page<QuestionBank> pageInfo = new Page<>(pageNum, pageSize);
        QueryWrapper<QuestionBank> wrapper = new QueryWrapper<>();
        wrapper
                .eq("module_id", moduleId)
                .orderByAsc("order_id");
        questionBankService.page(pageInfo, wrapper);
        for (QuestionBank record : pageInfo.getRecords()) {
            record.setChoiceList(Arrays.asList(record.getChoice().split("\\s+")));
        }

        return Result.success(pageInfo);
    }

}

