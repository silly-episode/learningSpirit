package com.boot.learningspirit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.learningspirit.entity.QuestionBank;
import com.boot.learningspirit.service.QuestionBankService;
import com.boot.learningspirit.utils.EncryptUtil;
import com.boot.learningspirit.utils.JwtUtil;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private JwtUtil jwtUtil;

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


    @Test
    public void test1() throws Exception {
        String openId = "1";
        String session = null;
        Map<String, String> sessionMap = new HashMap<>();
//        sessionMap.put("sessionKey", sessionKey);
        sessionMap.put("openid", openId);
        session = JSONObject.fromObject(sessionMap).toString();
        EncryptUtil encryptUtil = new EncryptUtil();
        session = encryptUtil.encrypt(session);
        String token = jwtUtil.getToken(session);
        System.out.println(token);
        String token2 = jwtUtil.getOpenidFromToken(token);
        System.out.println(token2);
    }

}
