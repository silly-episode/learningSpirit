package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.LoginLogDao;
import com.boot.learningspirit.entity.LoginLog;
import com.boot.learningspirit.service.LoginLogService;
import org.springframework.stereotype.Service;

/**
 * (LoginLog)表服务实现类
 *
 * @author makejava
 * @since 2023-04-01 17:07:34
 */
@Service("loginLogService")
public class LoginLogServiceImpl extends ServiceImpl<LoginLogDao, LoginLog> implements LoginLogService {

}

