package com.boot.learningspirit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.learningspirit.entity.BanJi;

import java.util.List;

/**
 * 班级表(Class)表服务接口
 *
 * @author makejava
 * @since 2023-03-01 16:36:03
 */
public interface ClassService extends IService<BanJi> {


    List<BanJi> getBanJiList(String openId);

    List<BanJi> classPage(String queryName, Integer offSet, Integer limit);
}

