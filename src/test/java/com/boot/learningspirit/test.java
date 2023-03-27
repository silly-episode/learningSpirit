package com.boot.learningspirit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.learningspirit.entity.QuestionBank;
import com.boot.learningspirit.service.QuestionBankService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/3/25 10:22
 * @FileName: test
 * @Description:
 */

@SpringBootTest
public class test {

    @Resource
    private QuestionBankService questionBankService;


    @Test
    public static void main(String[] args) {

        String a = "123";
        String b = "123,456";

        System.out.println(Arrays.toString(a.split(",")));
    }


    @Test
    public void test() {
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .orderByAsc("question_id")
                .orderBy(false, false, "order_id")
                .eq("module_id", "288117924286173184");
        List<QuestionBank> list = questionBankService.list(queryWrapper);
        for (QuestionBank bank : list) {
            System.out.println(bank.getQuestionId());
        }

    }

}
