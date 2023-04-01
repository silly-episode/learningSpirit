package com.boot.learningspirit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.learningspirit.dao.ClassDao;
import com.boot.learningspirit.entity.BanJi;
import com.boot.learningspirit.service.ClassService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 班级表(Class)表服务实现类
 *
 * @author makejava
 * @since 2023-03-01 16:36:03
 */
@Service("classService")
public class ClassServiceImpl extends ServiceImpl<ClassDao, BanJi> implements ClassService {
    @Resource
    private ClassDao classDao;

    @Override
    public List<BanJi> getBanJiList(String openId) {
        return classDao.getBanJiList(openId);
    }

    @Override
    public List<BanJi> classPage(String queryName, Integer offSet, Integer limit) {
        return classDao.classPage(queryName, offSet, limit);
    }
}

