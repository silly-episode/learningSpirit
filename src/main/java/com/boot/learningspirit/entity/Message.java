package com.boot.learningspirit.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息表（）(Message)表实体类
 *
 * @author makejava
 * @since 2023-03-28 20:01:32
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Message extends Model<Message> {
    //消息ID
    @TableId(type = IdType.INPUT)
    private Long msgId;
    //消息类别
    private Integer msgType;
    //消息标题
    private String msgTitle;
    //内容
    private String msgContent;
    //加入人id
    private String openId;
    //加入班级id
    private Long classId;
    //加入人角色类型
    private String type;
    //任务Id
    private Long taskId;
    //任务类型
    private String taskType;
    //消息产生时间
    private LocalDateTime messageCreateTime;


//    @TableField(exist = false)
//    private List<MessageReceive> MessageReceive;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.msgId;
    }
}

