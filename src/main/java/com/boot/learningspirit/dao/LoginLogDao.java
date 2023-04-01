package com.boot.learningspirit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.learningspirit.entity.LoginLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (LoginLog)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-01 17:07:34
 */
public interface LoginLogDao extends BaseMapper<LoginLog> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<LoginLog> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<LoginLog> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<LoginLog> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<LoginLog> entities);

}

