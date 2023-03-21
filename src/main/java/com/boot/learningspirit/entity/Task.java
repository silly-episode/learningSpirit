package com.boot.learningspirit.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务表(Task)表实体类
 *
 * @author makejava
 * @since 2023-03-02 14:15:18
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task extends Model<Task> {
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long taskId;
    //发布人id
    private String openId;
    //任务类型：work,notice,tianbiao,jielong
    private String typeWork;
    //任务标题 测试作业标题
    private String title;
    //测试作业内容	任务内容
    private String content;
    //文件列表 ,隔开
    private String fileList;
    //问题列表   ["学生姓名", "性别", "请输入手机号"]	只有部分填表类型的任务有该字段
    private String questionList;
    //只有作业类型和部分填表类型的任务有该字段

    //文字、图片、视频、文件、无需在线提交（中文字段可以吗？不行的话我再改成英文）
    private String way;
    //接收的班级id列表 ,隔开
    private String receiveClassList;
    //截至时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deadline;
    //定时发布时间   为空即不定时，现在就发布
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime fixTime;
    //    是否为草稿
    private Boolean isDraft;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.taskId;
    }
}

