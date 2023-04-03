package com.boot.learningspirit.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.learningspirit.common.excel.ExcelListener;
import com.boot.learningspirit.common.result.Result;
import com.boot.learningspirit.dto.ClassPage;
import com.boot.learningspirit.entity.QuestionBank;
import com.boot.learningspirit.entity.Task;
import com.boot.learningspirit.entity.User;
import com.boot.learningspirit.service.QuestionBankService;
import com.boot.learningspirit.service.TaskService;
import com.boot.learningspirit.service.UserService;
import com.boot.learningspirit.utils.JwtUtil;
import com.boot.learningspirit.utils.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @Resource
    private TaskService taskService;
    @Autowired
    JwtUtil jwtUtil;
    @Resource
    private UserService userService;

    /**
     * @param file:
     * @param name:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 上传题库
     * @Date: 2023/3/26 13:15
     */
    @PostMapping("upload")
    public Result upload(MultipartFile file, @RequestParam String name, HttpServletRequest request) throws IOException {
        //获取请求头token
        String token = request.getHeader("Authorization");
        //从token中获取openid
        String openid = jwtUtil.getOpenidFromToken(token);

        String module = name.substring(0, name.lastIndexOf("."));
        Long moduleId = SnowFlakeUtil.getNextId();
        EasyExcel.read(
                        file.getInputStream(),
                        QuestionBank.class,
                        new ExcelListener(questionBankService, moduleId, module, openid))
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
        for (QuestionBank bank : list) {
            User uploadUser = userService.getById(bank.getUploadId());
            if (uploadUser != null) {
                bank.setUploadName(uploadUser.getUserName());
            }
        }
        return Result.success(list);
    }


    /**
     * @param :
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 获取题库列表
     * @Date: 2023/3/21 11:30
     */
    @PostMapping("getBankList")
    public Result getBankList(@RequestBody ClassPage bankSearch) {
        Page<QuestionBank> pageInfo = new Page<>(bankSearch.getPageNum(), bankSearch.getPageSize());
        QueryWrapper<QuestionBank> wrapper = new QueryWrapper<>();
        wrapper
                .select("module_id , module ,question_create_time ,count(1) as bank_count")
                .like(!"".equals(bankSearch.getQueryName()), "module", bankSearch.getQueryName())
                .groupBy("module_id", "module", "question_create_time")
                .orderByDesc("question_create_time");
        questionBankService.page(pageInfo, wrapper);
//        根据QuestionBank中的moduleId对list去重
//        list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
//                -> new TreeSet<>(Comparator.comparing(QuestionBank::getModuleId))), ArrayList::new));
        for (QuestionBank bank : pageInfo.getRecords()) {
            User uploadUser = userService.getById(bank.getUploadId());
            if (uploadUser != null) {
                bank.setUploadName(uploadUser.getUserName());
            }
        }
        return Result.success(pageInfo);
    }

    /**
     * @param moduleId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 删除题库
     * @Date: 2023/3/27 11:41
     */
    @GetMapping("deleteBank")
    public Result deleteBank(@RequestParam Long moduleId) {
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("module_id", moduleId);
        if (questionBankService.remove(queryWrapper)) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
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

    /**
     * @param taskId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 获取考试题目
     * @Date: 2023/3/26 14:49
     */
    @GetMapping("getExamQuestionList")
    public Result getExamQuestionList(@RequestParam Long taskId) {

        Task task = taskService.getById(taskId);
        int qNumber = task.getQNumber();
        boolean random = task.getRandom();
        Long moduleId = task.getModuleId();
        if (task.getModuleId() == null) {
            return Result.error("该任务并非测验类型");
        }
        Set<Integer> set = new HashSet<>(qNumber);
        if (random) {
            Random ran = new Random();
            Long bankMaxCount = questionBankService.count(
                    new QueryWrapper<QuestionBank>()
                            .eq("module_id", moduleId));

            while (set.size() != qNumber) {
                System.out.println("123");
                Integer r = ran.nextInt(Math.toIntExact(bankMaxCount)) + 1;
                System.out.println(r);
                set.add(r);
            }
            System.out.println(set.toString());
            System.out.println(bankMaxCount);
        } else {
            int i = 1;
            while (set.size() != qNumber) {
                set.add(i++);
            }
        }
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("module_id", moduleId)
                .in("order_id", set)
                .orderByAsc("order_id");
        List<QuestionBank> list = questionBankService.list(queryWrapper);

        for (QuestionBank bank : list) {
            bank.setChoiceList(Arrays.asList(bank.getChoice().split("\\s+")));
        }
        return Result.success(list);
    }

    /**
     * @param moduleId:
     * @param moduleName:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 更改题库名字
     * @Date: 2023/4/1 16:00
     */
    @GetMapping("updateBankName")
    public Result updateBankName(@RequestParam Long moduleId, @RequestParam String moduleName) {
        UpdateWrapper<QuestionBank> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .set(!"".equals(moduleName) && moduleName != null, "module", moduleName)
                .eq("module_id", moduleId);
        if (questionBankService.update(updateWrapper)) {
            return Result.success();
        } else {
            return Result.error("更改失败");
        }
    }

}

