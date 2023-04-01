package com.boot.learningspirit.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2023-02-20 09:54:56
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<User> {
    //openId
    @TableId(type = IdType.ASSIGN_ID)
    private String openId;
    //用户名
    private String userName;
    //角色
    private String role;
    //性别

    private String sex;
    //电话
    private String tel;
    //所教的科目，多个科目之间用,区别
    private String subject;

    private LocalDateTime registerTime;

    private int userStatus;

    private String email;
    @TableField(exist = false)
    private LocalDateTime statusTime;
    @TableField(exist = false)
    private int taskCount;
    @TableField(exist = false)
    private List<BanJi> banJiList;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.openId;
    }
}

