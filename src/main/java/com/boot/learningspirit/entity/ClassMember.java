package com.boot.learningspirit.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 班级成员表(ClassMember)表实体类
 *
 * @author makejava
 * @since 2023-03-01 20:46:51
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassMember extends Model<ClassMember> {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long classId;

    private String openId;

    private String type;

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

