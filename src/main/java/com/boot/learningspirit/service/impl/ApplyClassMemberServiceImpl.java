package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.ApplyClassMemberDao;
import com.boot.learningspirit.entity.ApplyClassMember;
import com.boot.learningspirit.service.ApplyClassMemberService;
import org.springframework.stereotype.Service;

/**
 * (ApplyClassMember)表服务实现类
 *
 * @author makejava
 * @since 2023-03-27 08:42:30
 */
@Service("applyClassMemberService")
public class ApplyClassMemberServiceImpl extends ServiceImpl<ApplyClassMemberDao, ApplyClassMember> implements ApplyClassMemberService {

}

