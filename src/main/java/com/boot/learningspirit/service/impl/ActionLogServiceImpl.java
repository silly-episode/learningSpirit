package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.ActionLogDao;
import com.boot.learningspirit.entity.ActionLog;
import com.boot.learningspirit.service.ActionLogService;
import org.springframework.stereotype.Service;

/**
 * (ActionLog)表服务实现类
 *
 * @author makejava
 * @since 2023-04-20 10:50:37
 */
@Service("actionLogService")
public class ActionLogServiceImpl extends ServiceImpl<ActionLogDao, ActionLog> implements ActionLogService {

}

