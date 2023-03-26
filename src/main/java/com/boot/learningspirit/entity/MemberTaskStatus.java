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
    //    学生留言
    private String msg;
    //    老师评语
    private String remark;
    //    评级
    private int rate;
    //任务类型，接龙，填表，通知；测验，作业
    private String type;
    //    文件列表,逗号区分
    private String fileList;
    //    接龙和通知的确认
    private Boolean confirm;
    // 测验的成绩
    private int grade;
    // 老师是否批改
    private Boolean markStatus;
    // 答案列表,用逗号区分
    private String answerList;
    //    班级已经完成的人数
    @TableField(exist = false)
    private int countStatus;
    //    用户名
    @TableField(exist = false)
    private String userName;


    public MemberTaskStatus(Long taskId, String openId,
                            LocalDateTime statusTime,
                            Long classId, String type) {
        this.taskId = taskId;
        this.openId = openId;
        this.statusTime = statusTime;
        this.classId = classId;
        this.type = type;
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

