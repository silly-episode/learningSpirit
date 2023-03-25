package com.boot.learningspirit.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 班级表(Class)表实体类
 *
 * @author makejava
 * @since 2023-03-01 16:36:03
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BanJi extends Model<BanJi> {
    //    手动创建id
    @TableId(type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long classId;

    private String className;

    private Integer classNum;
    //创建者
    private String classCreator;
    //管理员，可转让，创建者为默认管理员
    private String classAdmin;

    private LocalDateTime classCreateTime;
    //已经加入班级人数
    private Integer joined;
    //    非学生人数
    private Integer teacherCount;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.classId;
    }
}

