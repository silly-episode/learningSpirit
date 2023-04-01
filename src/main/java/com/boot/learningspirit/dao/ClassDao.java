package com.boot.learningspirit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.learningspirit.entity.BanJi;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 班级表(Class)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-01 16:36:03
 */
@Repository
public interface ClassDao extends BaseMapper<BanJi> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Class> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<BanJi> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Class> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<BanJi> entities);


    List<BanJi> getBanJiList(@Param("openId") String openId);

    @Select("select " +
            "(select count(1) from learningspirit.ban_ji b " +
            "  left join learningspirit.user x on b.class_admin=x.open_id " +
            "  left join learningspirit.user y on b.class_creator=y.open_id " +
            "  where b.class_name like #{queryName} or " +
            "  x.user_name like #{queryName} or " +
            "  y.user_name like #{queryName}) " +
            " as totalCount , b.* ,x.user_name as adminName,y.user_name as creatorName from learningspirit.ban_ji b " +
            "left join learningspirit.user x on b.class_admin=x.open_id " +
            "left join learningspirit.user y on b.class_creator=y.open_id " +
            "where b.class_name like #{queryName} or " +
            "x.user_name like #{queryName} or " +
            "y.user_name like #{queryName} order by b.class_create_time limit #{offSet},#{limit}")
    List<BanJi> classPage(String queryName, Integer offSet, Integer limit);

}

