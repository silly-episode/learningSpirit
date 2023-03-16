package com.boot.learningspirit.service.impl;

import com.boot.learningspirit.dao.ClassMemberDao;
import com.boot.learningspirit.entity.ClassMember;
import com.boot.learningspirit.service.ClassMemberService;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 班级成员表(ClassMember)表服务实现类
 *
 * @author makejava
 * @since 2023-03-01 20:46:51
 */
@Service("classMemberService")
public class ClassMemberServiceImpl extends MppServiceImpl<ClassMemberDao, ClassMember> implements ClassMemberService {

}

