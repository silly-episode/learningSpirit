package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.MemberTaskStatusDao;
import com.boot.learningspirit.entity.MemberTaskStatus;
import com.boot.learningspirit.service.MemberTaskStatusService;
import org.springframework.stereotype.Service;

/**
 * 个人任务完成情况(MemberTaskStatus)表服务实现类
 *
 * @author makejava
 * @since 2023-03-24 12:12:11
 */
@Service("memberTaskStatusService")
public class MemberTaskStatusServiceImpl extends ServiceImpl<MemberTaskStatusDao, MemberTaskStatus> implements MemberTaskStatusService {

}

