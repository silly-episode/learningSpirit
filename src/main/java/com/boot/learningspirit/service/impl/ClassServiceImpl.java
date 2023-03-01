package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.ClassDao;
import com.boot.learningspirit.entity.BanJi;
import com.boot.learningspirit.service.ClassService;
import org.springframework.stereotype.Service;

/**
 * 班级表(Class)表服务实现类
 *
 * @author makejava
 * @since 2023-03-01 16:36:03
 */
@Service("classService")
public class ClassServiceImpl extends ServiceImpl<ClassDao, BanJi> implements ClassService {

}

