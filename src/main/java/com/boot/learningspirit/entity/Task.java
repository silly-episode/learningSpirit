package com.boot.learningspirit.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
import java.util.List;

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
    @TableId(type = IdType.INPUT)
    private Long taskId;
    //发布人id
    private String openId;
    //班级事务(affair)：notice、jielong、tianbiao
    //学习任务（learn）：work、exam
    private String type;
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
    //    题目是否随机
    private Boolean random;
    // 题库id
    private Long moduleId;
    // 题目数量
    private Integer qNumber;
    //    发布时间
    private LocalDateTime publishTime;
    //    已完成人数
    @TableField(exist = false)
    private int completeNum;
    //    未完成人数
    @TableField(exist = false)
    private int incompleteNum;
    //    班级加入
    @TableField(exist = false)
    private int joined;
    // 完成比率
    @TableField(exist = false)
    private String completionRate;
    //    实际班级名称
    @TableField(exist = false)
    private String className;
    // 实际班级id
    @TableField(exist = false)
    private Long classId;
    //    发布者
    @TableField(exist = false)
    private String publisher;
    //    详细状态
    @TableField(exist = false)
    private String status;
    //    未完成的人的姓名
    @TableField(exist = false)
    private List<String> incompletedList;
    //    已经完成的人的信息
    @TableField(exist = false)
    private List<MemberTaskStatus> completedList;
    @TableField(exist = false)
    private MemberTaskStatus memberTaskStatus;
    @TableField(exist = false)
    private String bankName;

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

