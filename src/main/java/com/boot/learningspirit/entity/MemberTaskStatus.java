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

/**
 * 个人任务完成情况(MemberTaskStatus)表实体类
 *
 * @author makejava
 * @since 2023-03-24 12:15:45
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberTaskStatus extends Model<MemberTaskStatus> {
    //任务情况id
    @TableId(type = IdType.ASSIGN_ID)
    private Long statusId;
    //任务id
    private Long taskId;
    //用户id
    private String openId;
    //个人任务完成情况
    private String status;
    //状态时间
    private LocalDateTime statusTime;
    //班级id
    private Long classId;
    @TableField(exist = false)
    //    班级已经完成的人数
    private int countStatus;


    public MemberTaskStatus(Long taskId, String openId, LocalDateTime statusTime, Long classId) {
        this.taskId = taskId;
        this.openId = openId;
        this.statusTime = statusTime;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.statusId;
    }
}

