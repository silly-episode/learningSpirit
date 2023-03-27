package com.boot.learningspirit.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (ApplyClassMember)表实体类
 *
 * @author makejava
 * @since 2023-03-27 08:44:29
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyClassMember extends Model<ApplyClassMember> {
    //主键
    private Long applyId;
    //用户id
    private String openId;
    //申请加入班级的id
    private Long classId;
    //申请加入时间
    private LocalDateTime applyTime;
    //身份
    private String type;
    //申请结果
    private String result;
    //是否处理
    private Boolean deal;
    //处理人id
    private String dealOpenId;
    //申请班级名称
    @TableField(exist = false)
    private String className;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.applyId;
    }
}

