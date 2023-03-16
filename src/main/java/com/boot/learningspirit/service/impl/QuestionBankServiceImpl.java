package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.QuestionBankDao;
import com.boot.learningspirit.entity.QuestionBank;
import com.boot.learningspirit.service.QuestionBankService;
import org.springframework.stereotype.Service;

/**
 * 题库(QuestionBank)表服务实现类
 *
 * @author makejava
 * @since 2023-03-16 10:37:38
 */
@Service("questionBankService")
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankDao, QuestionBank> implements QuestionBankService {

}

